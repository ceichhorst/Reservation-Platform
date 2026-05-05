package com.ceichhorst.reservation.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ceichhorst.reservation.dao.AdministratorDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ceichhorst.reservation.auth.*;
import com.ceichhorst.reservation.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.ceichhorst.reservation.entity.Administrator;


@WebServlet(
        urlPatterns = {"/auth"}
)

/**
 * Inspired by: https://stackoverflow.com/questions/52144721/how-to-get-access-token-using-client-credentials-using-java-code
 */

public class Auth extends HttpServlet implements PropertiesLoader {

    Properties properties;
    String CLIENT_ID;
    String CLIENT_SECRET;
    String OAUTH_URL;
    String LOGIN_URL;
    String REDIRECT_URL;
    String REGION;
    String POOL_ID;
    Keys jwks;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void init() throws ServletException {
        super.init();
        loadProperties();
        loadKey();
    }

    /**
     * Gets the auth code from the request and exchanges it for a token containing user info.
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authCode = req.getParameter("code");
        String userName;
        String userEmail;
        String role;

        if (authCode == null || authCode.isEmpty()) {
            logger.warn("No auth code received in /auth request.");
            req.setAttribute("errorMessage", "Login failed: missing authorization code.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
            return;
        } else {
            HttpRequest authRequest = buildAuthRequest(authCode);
            try {
                TokenResponse tokenResponse = getToken(authRequest);
                Map<String, String> authData = validate(tokenResponse);

                userName = authData.get("username");
                userEmail = authData.get("email");
                role = authData.get("role");

                AdministratorDao administratorDao = new AdministratorDao();
                Administrator administrator = administratorDao.getAdministratorByEmail(userEmail);

                if (administrator == null) {
                    administrator = new Administrator();
                    administrator.setUsername(userName);
                    administrator.setEmail(userEmail);
                    administrator.setRole(role);
                    administrator.setCreatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());

                    // TODO Make note of admin_restaurant join table
                    // TODO Consider implementing at login or manually assigned via 'Dyana administrators'

                    administratorDao.save(administrator);
                }

                // Last login
                administrator.setLastLogin(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
                administratorDao.update(administrator);

                HttpSession session = req.getSession();
                session.setAttribute("adminId", administrator.getId());
                session.setAttribute("userEmail", userEmail);
                session.setAttribute("role", role);

            } catch (IOException e) {
                logger.error("Error getting or validating the token: " + e.getMessage(), e);
                req.setAttribute("errorMessage", "Login failed: error validating token");
                RequestDispatcher dispatcher = req.getRequestDispatcher("/error.jsp");
                dispatcher.forward(req, resp);
                return;
            } catch (InterruptedException e) {
                logger.error("Error getting token from Cognito oauth url " + e.getMessage(), e);
                req.setAttribute("errorMessage", "Login failed: error getting OAuth token.");
                RequestDispatcher dispatcher = req.getRequestDispatcher("/error.jsp");
                dispatcher.forward(req, resp);
                return;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");

    }

    /**
     * Sends the request for a token to Cognito and maps the response
     * @param authRequest the request to the oauth2/token url in cognito
     * @return response from the oauth2/token endpoint which should include id token, access token and refresh token
     * @throws IOException
     * @throws InterruptedException
     */
    private TokenResponse getToken(HttpRequest authRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<?> response = null;

        response = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("STATUS: " + response.statusCode());
            System.out.println("ERROR BODY: " + response.body());

            throw new IOException("Failed to get token from Cognito");
        }


        logger.debug("Response headers: " + response.headers().toString());
        logger.debug("Response body: " + response.body().toString());

        ObjectMapper mapper = new ObjectMapper();
        TokenResponse tokenResponse = mapper.readValue(response.body().toString(), TokenResponse.class);
        logger.debug("Id token: " + tokenResponse.getIdToken());

        return tokenResponse;

    }

    /**
     * Get values out of the header to verify the token is legit. If it is legit, get the claims from it, such
     * as username.
     * @param tokenResponse
     * @return
     * @throws IOException
     */
    private Map<String, String> validate(TokenResponse tokenResponse) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        CognitoTokenHeader tokenHeader = mapper.readValue(CognitoJWTParser.getHeader(tokenResponse.getIdToken()).toString(), CognitoTokenHeader.class);

        // Header should have kid and alg- https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-the-id-token.html
        String keyId = tokenHeader.getKid();
        String alg = tokenHeader.getAlg();

        /**
         * Search the KeysItem list from jwks.getKeys() [Keys class] for the one 'kid' that matches
         *
         * This ensures we are using/finding the exact key being used than just assuming.
         *
         * Source for more info on filter(), findFirst(): https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
         */
        KeysItem signingKey = jwks.getKeys().stream().filter(k -> k.getKid().equals(keyId)).findFirst().orElseThrow(() -> new ServletException("Unable to find matching key for kid: " + keyId));
        // Use Key's N and E
        BigInteger modulus = new BigInteger(1,
                java.util.Base64.getUrlDecoder().decode(signingKey.getN()));

        BigInteger exponent = new BigInteger(1,
                java.util.Base64.getUrlDecoder().decode(signingKey.getE()));

        // Create a public key
        PublicKey publicKey = null;
        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (InvalidKeySpecException e) {
            logger.error("Invalid Key Error " + e.getMessage(), e);
            throw new ServletException("Unable to generate key for validation.");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Algorithm Error " + e.getMessage(), e);
            throw new ServletException("Unable to generate key for validation.");
        }

        // get an algorithm instance
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);

        // Verify ISS field of the token to make sure it's from the Cognito source
        String iss = String.format("https://cognito-idp.%s.amazonaws.com/%s", REGION, POOL_ID);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(iss)
                .withClaim("token_use", "id") // make sure you're verifying id token
                .build();

        // Verify the token
        DecodedJWT jwt = verifier.verify(tokenResponse.getIdToken());
        List<String> groups = jwt.getClaim("cognito:groups").asList(String.class);
        String userName = jwt.getClaim("cognito:username").asString();
        String userEmail = jwt.getClaim("email").asString();

        String role = "USER";
        if (groups != null && groups.contains("ADMIN")) {
            role = "ADMIN";
        }

        logger.debug("here's the userEmail: " + userEmail);
        logger.debug("here's the role: " + role);
        logger.debug("here are all the available claims: " + jwt.getClaims());

        // keeping it simple and just returning the userEmail
        Map<String, String> result = new HashMap();
        result.put("username", userName);
        result.put("email", userEmail);
        result.put("role", role);
        return result;

    }

    /** Create the auth url and use it to build the request.
     *
     * @param authCode auth code received from Cognito as part of the login process
     * @return the constructed oauth request
     */
    private HttpRequest buildAuthRequest(String authCode) {
        String keys = CLIENT_ID + ":" + CLIENT_SECRET;

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "authorization_code");
        parameters.put("client_secret", CLIENT_SECRET);
        parameters.put("client_id", CLIENT_ID);
        parameters.put("code", authCode);
        parameters.put("redirect_uri", REDIRECT_URL);

        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String encoding = Base64.getEncoder().encodeToString(keys.getBytes());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OAUTH_URL))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + encoding)
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();
        return request;
    }

    /**
     * Gets the JSON Web Key Set (JWKS) for the user pool from cognito and loads it
     * into objects for easier use.
     *
     * JSON Web Key Set (JWKS) location: https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json
     * Demo url: https://cognito-idp.us-east-2.amazonaws.com/us-east-2_XaRYHsmKB/.well-known/jwks.json
     *
     * @see Keys
     * @see KeysItem
     */
    private void loadKey() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            URL jwksURL = new URL(String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", REGION, POOL_ID));

            jwks = mapper.readValue(jwksURL, Keys.class);
            logger.debug("Keys are loaded. Here's e: " + jwks.getKeys().get(0).getE());
        } catch (IOException ioException) {
            logger.error("Cannot load json..." + ioException.getMessage(), ioException);
        } catch (Exception e) {
            logger.error("Error loading json" + e.getMessage(), e);
        }
    }

    /**
     * Read in the cognito props file and get/set the client id, secret, and required urls
     * for authenticating a user.
     */
    private void loadProperties() {

        properties = (Properties) getServletContext().getAttribute("cognitoProperties");
        if (properties == null) {
            logger.error("Cognito properties missing in ServletContext");
            return;
        }
        CLIENT_ID = properties.getProperty("client.id");
        CLIENT_SECRET = properties.getProperty("client.secret");
        OAUTH_URL = properties.getProperty("oauthURL");
        LOGIN_URL = properties.getProperty("loginURL");
        REDIRECT_URL = properties.getProperty("redirectURL");
        REGION = properties.getProperty("region");
        POOL_ID = properties.getProperty("poolId");

    }
}

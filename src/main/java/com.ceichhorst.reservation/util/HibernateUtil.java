public class HiberanteUtil {

    private static final SessionFactory sessionFactory =
            new Configuration().configure().buildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
const dateSelect = document.getElementById("dateSelect");
const timeSelect = document.getElementById("timeSelect");

const restaurantId = document.getElementById("restaurantId").value;

const schedulingType = document.getElementById("schedulingType").value;

const loadTimes = async (date) => {

    if (!date) {
        timeSelect.innerHTML = `<option value="">Select a Time</option>`;
        return;
    }

    try {
        const response = await fetch(
            `${window.contextPath}/api/availability?restaurantId=${restaurantId}&date=${date}`
        );

        if (!response.ok) {
            throw new Error("Failed to load availability");
        }

        const data = await response.json();

        timeSelect.innerHTML = "";

        if (schedulingType === "DATE_ONLY") {
            if (data.length > 0) {
                const slot = data[0];
                const option = document.createElement("option");
                option.value = slot.id;
                option.textContent = `${slot.serviceTimeFormatted} (assigned automatically)`;
                option.selected = true;
                timeSelect.appendChild(option);

                timeSelect.disabled = true;
            } else {
                const option = document.createElement("option");
                option.value = "";
                option.textContent = "No availability";
                timeSelect.appendChild(option);
                timeSelect.disabled = true
            }

            return;
        }

        // Other scheduling types
        timeSelect.disabled = false;
        const defaultOption = document.createElement("option");

        defaultOption.value = "";
        defaultOption.textContent = "Select a Time";

        timeSelect.appendChild(defaultOption);

        data.forEach(slot => {

            const option = document.createElement("option");

            option.value = slot.id;

            option.textContent = slot.serviceTimeFormatted;

            timeSelect.appendChild(option);
        });

    } catch (err) {
        console.error("Error loading times:", err);
        timeSelect.innerHTML = `<option value="">Error loading times</option>`;
    }
};

dateSelect.addEventListener("change", (e) => {
    loadTimes(e.target.value);
});
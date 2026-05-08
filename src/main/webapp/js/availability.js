const dateSelect = document.getElementById("dateSelect");
const timeSelect = document.getElementById("timeSelect");
const partySizeSelect = document.querySelector('select[name="partySize"]');

const restaurantId = document.getElementById("restaurantId").value;
const schedulingType = document.getElementById("schedulingType").value;

const allPartySizeOptions = Array.from(partySizeSelect.options).map(o => ({
    value: o.value,
    text: o.textContent
}));

const updatePartySizeOptions = (remainingSeats) => {
    const currentValue = partySizeSelect.value;
    partySizeSelect.innerHTML = "";

    allPartySizeOptions.forEach(opt => {
        if (opt.value === "" || parseInt(opt.value) <= remainingSeats) {
            const o = document.createElement("option");
            o.value = opt.value;
            o.textContent = opt.text;
            partySizeSelect.appendChild(o);
        }
    });

    // Restore previous selection is still valid, otherwise reset
    const stillValid = Array.from(partySizeSelect.options).some(o => o.value === currentValue);
    partySizeSelect.value = stillValid ? currentValue : "";
}

const restorePartySizeOptions = () => {
    const currentValue = partySizeSelect.value;
    partySizeSelect.innerHTML = "";

    allPartySizeOptions.forEach(opt => {
        const o = document.createElement("option");
        o.value = opt.value;
        o.textContent = opt.text;
        partySizeSelect.appendChild(o);
    });

    partySizeSelect.value = currentValue;
};

const loadTimes = async (date) => {

    if (!date) {
        timeSelect.innerHTML = `<option value="">Select a Time</option>`;
        restorePartySizeOptions();
        return;
    }

    const partySize = partySizeSelect.value || 1;

    const previousTimeValue = timeSelect.value;

    try {
        const response = await fetch(
            `${window.contextPath}/api/availability?restaurantId=${restaurantId}&date=${date}&partySize=${partySize}`
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
                updatePartySizeOptions(slot.remainingSeats);
            } else {
                const option = document.createElement("option");
                option.value = "";
                option.textContent = "No availability";
                timeSelect.appendChild(option);
                timeSelect.disabled = true
                restorePartySizeOptions();
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
            option.dataset.remainingSeats = slot.remainingSeats;
            timeSelect.appendChild(option);
        });


        // restore time if still available
        const stillAvailable = Array.from(timeSelect.options).some(o => o.value === previousTimeValue);
        if (stillAvailable && previousTimeValue) {
            timeSelect.value = previousTimeValue;
            const restoredOption = timeSelect.options[timeSelect.selectedIndex];
            const remaining = parseInt(restoredOption.dataset.remainingSeats);
            if (!isNaN(remaining)) {
                updatePartySizeOptions(remaining);
            }
        } else {
            restorePartySizeOptions();
        }

    } catch (err) {
        console.error("Error loading times:", err);
        timeSelect.innerHTML = `<option value="">Error loading times</option>`;
    }
};

// Date change -> reload times
dateSelect.addEventListener("change", (e) => {
    loadTimes(e.target.value);
});

// Time change -> filter party size dropdwon to remaining seats
timeSelect.addEventListener("change", () => {
    const selected = timeSelect.options[timeSelect.selectedIndex];
    const remaining = selected ? parseInt(selected.dataset.remainingSeats) : null;

    if (remaining != null && !isNaN(remaining)) {
        updatePartySizeOptions(remaining);
    } else {
        restorePartySizeOptions();
    }
})

// Party size change -> re-fetch times
partySizeSelect.addEventListener("change", () => {
    if (dateSelect.value) {
        loadTimes(dateSelect.value);
    }
});
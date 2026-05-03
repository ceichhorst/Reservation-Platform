const toggleFilterInputs = () => {
    const type = document.querySelector('[name="filterType"]').value;

    document.getElementById("dateInput").style.display =
        type === "DATE" ? "inline-block" : "none";

    document.getElementById("monthInput").style.display =
        type === "MONTH" ? "inline-block" : "none";
};

document.addEventListener("DOMContentLoaded", toggleFilterInputs);
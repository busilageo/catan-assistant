document.addEventListener("DOMContentLoaded", function() {
    var cells = document.querySelectorAll("td");

    cells.forEach(function(cell) {
        if (cell.textContent.trim() === "0") {
            cell.classList.add("transparent");
        }
    });
});
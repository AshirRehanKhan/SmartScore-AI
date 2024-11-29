const data = [
    { name: "John Doe", assignmentsCompleted: 10, totalAssignments: 12, attendancePresent: 18, attendanceTotal: 20 },
    { name: "Jane Smith", assignmentsCompleted: 8, totalAssignments: 12, attendancePresent: 15, attendanceTotal: 20 },
    { name: "Sam Brown", assignmentsCompleted: 5, totalAssignments: 12, attendancePresent: 10, attendanceTotal: 20 },
];

const tableBody = document.getElementById("table-body");
const modal = document.getElementById("add-student-modal");
const closeModal = document.querySelector(".close");
const addStudentForm = document.getElementById("add-student-form");
const addBtn = document.querySelector('.add-new');
const searchInput = document.querySelector('.search-bar');

function populateTable() {
    tableBody.innerHTML = "";
    data.forEach((student) => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>Image</td>
            <td>${student.name}</td>
            <td>${student.assignmentsCompleted}/${student.totalAssignments}</td>
            <td>${student.attendancePresent}/${student.attendanceTotal}</td>
            <td><canvas class="pie-chart" width="100" height="100"></canvas></td>
            <td><canvas class="bar-chart" width="100" height="100"></canvas></td>
        `;

        tableBody.appendChild(row);

        // Pie Chart
        const pieCtx = row.querySelector(".pie-chart").getContext("2d");
        new Chart(pieCtx, {
            type: "pie",
            data: {
                labels: ["Completed", "Pending"],
                datasets: [{
                    data: [student.assignmentsCompleted, student.totalAssignments - student.assignmentsCompleted],
                    backgroundColor: ["#4CAF50", "#FF7043"],
                }],
            },
        });

        // Bar Chart
        const barCtx = row.querySelector(".bar-chart").getContext("2d");
        new Chart(barCtx, {
            type: "bar",
            data: {
                labels: ["Presents", "Absents"],
                datasets: [{
                    data: [student.attendancePresent, student.attendanceTotal - student.attendancePresent],
                    backgroundColor: ["#2196F3", "#F44336"],
                }],
            },
        });
    });
}

// Populate table on load
populateTable();

// Open modal
addBtn.addEventListener('click', () => {
    modal.style.display = "flex";
});

// Close modal
closeModal.addEventListener('click', () => {
    modal.style.display = "none";
});

window.addEventListener('click', (e) => {
    if (e.target === modal) {
        modal.style.display = "none";
    }
});

// Add new student
addStudentForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const name = document.getElementById('name').value;
    const attendance = parseInt(document.getElementById('attendance').value);
    const assignments = parseInt(document.getElementById('assignments').value);

    const newStudent = {
        name,
        assignmentsCompleted: assignments,
        totalAssignments: 12,
        attendancePresent: attendance,
        attendanceTotal: 20,
    };

    data.push(newStudent);
    populateTable();
    modal.style.display = "none";
});

// Search functionality
searchInput.addEventListener('input', (e) => {
    const query = e.target.value.toLowerCase();
    document.querySelectorAll("#table-body tr").forEach((row) => {
        const name = row.querySelector("td:nth-child(2)").textContent.toLowerCase();
        row.style.display = name.includes(query) ? "" : "none";
    });
});
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    sidebar.classList.toggle('collapsed');
}

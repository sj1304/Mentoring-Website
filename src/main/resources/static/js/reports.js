/* ============================
   CHART INSTANCES
============================ */
let goalCategoryChartInstance = null;
let goalTimeChartInstance = null;
let mentorChartInstance = null;

/* ============================
   CREATE STUDENT CARDS
============================ */
document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("studentCards");
    const students = window.studentsData || [];

    if (container) {
        container.innerHTML = "";
        students.forEach((s, index) => {
            const card = document.createElement("div");
            card.className = "student-card";
            card.style.opacity = "0";
            card.style.transform = "translateY(40px)";

            card.innerHTML = `
                <h3>${s.firstName} ${s.lastName}</h3>
                <p>Roll: ${s.rollNo}</p>
                <div class="progress-bar">
                    <div class="progress" style="width: 0%; transition: width 1.2s ease-out;"></div>
                </div>
                <p><strong>${s.progress}%</strong> Goals Completed</p>
            `;
            container.appendChild(card);

            setTimeout(() => {
                card.style.transition = "all 0.6s ease";
                card.style.opacity = "1";
                card.style.transform = "translateY(0)";
                const bar = card.querySelector(".progress");
                setTimeout(() => { bar.style.width = s.progress + "%"; }, 400);
            }, index * 100);
        });
    }
});

/* ============================
   GOAL ANALYTICS CHARTS
============================ */
function loadGoalCharts() {
    const categories = window.goalCategoriesData || [];
    const timeline = window.goalTimelineData || {labels:[]};

    // Category Chart
    const categoryCtx = document.getElementById("goalCategoryChart");
    if (categoryCtx && categories.length > 0) {
        if (goalCategoryChartInstance) goalCategoryChartInstance.destroy();
        goalCategoryChartInstance = new Chart(categoryCtx, {
            type: "doughnut",
            data: {
                labels: categories.map(g => g.category || "Uncategorized"),
                datasets: [{
                    data: categories.map(g => g.count),
                    backgroundColor: ["#4CAF50", "#2196F3", "#FF9800", "#E91E63", "#9C27B0"]
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });
    }

    // Timeline Chart
    const timelineCtx = document.getElementById("goalTimeChart");
    if (timelineCtx && timeline.labels.length > 0) {
        if (goalTimeChartInstance) goalTimeChartInstance.destroy();
        goalTimeChartInstance = new Chart(timelineCtx, {
            type: "bar",
            data: {
                labels: timeline.labels,
                datasets: [
                    { label: "Created", data: timeline.createdData, backgroundColor: "#2196F3" },
                    { label: "Updated", data: timeline.updatedData, backgroundColor: "#FF9800" }
                ]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });
    }

    // Show boxes
    document.querySelectorAll("#goal_analytics .chart-box").forEach(box => {
        box.style.opacity = "1";
        box.style.transform = "translateY(0)";
    });
}

/* ============================
   MENTOR COMPARISON CHART
============================ */
function loadMentorChart() {
    const data = window.mentorPerformanceData || [];
    const mentorCtx = document.getElementById("mentorChart");

    if (mentorCtx && data.length > 0) {
        if (mentorChartInstance) mentorChartInstance.destroy();
        mentorChartInstance = new Chart(mentorCtx, {
            type: "bar",
            data: {
                labels: data.map(m => m.mentor),
                datasets: [{
                    label: "Avg Student Progress (%)",
                    data: data.map(m => m.completedGoals),
                    backgroundColor: "rgba(76, 175, 80, 0.8)",
                    borderRadius: 5
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                maintainAspectRatio: false,
                scales: { x: { beginAtZero: true, max: 100 } }
            }
        });

        const box = document.querySelector("#mentor_comparison .chart-box");
        if (box) {
            box.style.opacity = "1";
            box.style.transform = "translateY(0)";
        }
    }
}

/* ============================
   OBSERVER & INITIALIZATION
============================ */
const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            if (entry.target.id === "goal_analytics") loadGoalCharts();
            if (entry.target.id === "mentor_comparison") loadMentorChart();
        }
    });
}, { threshold: 0.15 });

const goalSection = document.getElementById("goal_analytics");
const mentorSection = document.getElementById("mentor_comparison");
if (goalSection) observer.observe(goalSection);
if (mentorSection) observer.observe(mentorSection);

// Fallback trigger
document.addEventListener("DOMContentLoaded", () => {
    setTimeout(() => {
        loadGoalCharts();
        loadMentorChart();
    }, 1500);
});
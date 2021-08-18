<script>
import { Line } from "vue-chartjs";

export default {
  props: ["chartDataLabels", "chartEstimatedWork", "hoursPerDay"],
  extends: Line,
  data() {
    return {
      chartdata: {
        labels: null,
        datasets: [
          {
            label: "Trabajo Estimado",
            type: "line",
            borderColor: "#36495d",
            borderWidth: 3,
            data: null,
            tension: 0,
          },
          {
            label: "Trabajo Real",
            type: "line",
            borderColor: "blue",
            borderWidth: 3,
            data: null,
            tension: 0,
          },
        ],
      },
      options: {
        scales: {
          y: {
            stacked: false,
          },
        },
      },
    };
  },

  mounted() {
    this.chartdata.labels = this.chartDataLabels;
    this.chartdata.datasets[0].data = this.chartEstimatedWork;
    this.chartdata.datasets[1].data = this.hoursPerDay;
    this.renderChart(this.chartdata, this.options);
  },

  /* watch: {
    chartDataLabels: function (newVal) {
      this.chartdata.labels = newVal;
    },
    chartEstimatedWork: function (newVal) {
      this.chartdata.datasets[0].data = newVal;
    },
  },*/
};
</script>



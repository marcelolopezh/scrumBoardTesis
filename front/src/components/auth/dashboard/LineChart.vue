<script>
import { Line } from "vue-chartjs";

export default {
  props: ["chartDataLabels", "chartEstimatedWork"],
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
          },
           {
            label: "Trabajo Real",
            type: "line",
            borderColor: "blue",
            borderWidth: 3,
            data: null,
          },
        ],
      },
      options: {
        type: Object,
        default: null,
      },
    };
  },

  mounted() {
    this.chartdata.labels = this.chartDataLabels;
    this.chartdata.datasets[0].data = this.chartEstimatedWork;
    this.chartdata.datasets[1].data = [58,58,58,53,51,50,49,30,20,10];
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



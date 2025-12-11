import Chart from 'https://cdn.jsdelivr.net/npm/chart.js@4.4.4/auto/+esm';
import zoomPlugin from 'https://cdn.jsdelivr.net/npm/chartjs-plugin-zoom@2.2.0/+esm';

const LABEL_PRECISION = 2;
const ZOOM_MIN_RANGE = 0.1;
const ZOOM_WHEEL_SPEED = 0.1;
const ZOOM_MAX_RATIO = 6;

export class ChartManager {
  constructor(canvasId = 'fn-chart') {
    this.canvasId = canvasId;
    this.chart = null;
    this.bounds = null;
  }

  destroy() {
    if (this.chart) {
      this.chart.destroy();
      this.chart = null;
    }
  }

  render(points = [], onPointSelect = null) {
    const canvas = document.getElementById(this.canvasId);
    if (!canvas) return;

    if (!points.length) {
      this.destroy();
      return;
    }
    Chart.register(zoomPlugin);

    const sorted = [...points].sort((a, b) => Number(a.xValue) - Number(b.xValue));
    const labels = sorted.map((p) => Number(p.xValue));
    const data = sorted.map((p) => ({ x: Number(p.xValue), y: Number(p.yValue) }));
    const numericY = data.map((p) => p.y).filter((v) => Number.isFinite(v));
    const numericX = data.map((p) => p.x).filter((v) => Number.isFinite(v));
    const minY = numericY.length ? Math.min(...numericY) : 0;
    const maxY = numericY.length ? Math.max(...numericY) : 0;
    const padding = (maxY - minY) * 0.1 || 1;
    const suggestedMin = minY - padding;
    const suggestedMax = maxY + padding;
    const minX = numericX.length ? Math.min(...numericX) : 0;
    const maxX = numericX.length ? Math.max(...numericX) : numericX.length;
    this.bounds = {
      xMin: minX,
      xMax: maxX,
      yMin: suggestedMin,
      yMax: suggestedMax,
    };

    if (this.chart) {
      this.chart.data.labels = labels;
      this.chart.data.datasets[0].data = data;
      this.chart.options.scales.y.suggestedMin = suggestedMin;
      this.chart.options.scales.y.suggestedMax = suggestedMax;
      this.chart.options.plugins.zoom.limits = {
        x: { min: minX, max: maxX, minRange: ZOOM_MIN_RANGE },
        y: { min: suggestedMin, max: suggestedMax, minRange: ZOOM_MIN_RANGE },
      };
      this.chart.resetZoom && this.chart.resetZoom();
      this.chart.update();
      return;
    }

    this.chart = new Chart(canvas, {
      type: 'line',
      data: {
        datasets: [
          {
            label: 'f(x)',
            data,
            borderColor: getComputedStyle(document.documentElement).getPropertyValue('--primary') || '#7c3aed',
            backgroundColor: 'rgba(124,58,237,0.15)',
            fill: true,
            tension: 0.25,
            pointRadius: 3,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        aspectRatio: 16 / 9,
        normalized: true,
        interaction: { mode: 'nearest', intersect: false },
        plugins: {
          legend: { display: false },
          zoom: {
            limits: {
              x: { min: minX, max: maxX, minRange: ZOOM_MIN_RANGE },
              y: { min: suggestedMin, max: suggestedMax, minRange: ZOOM_MIN_RANGE },
            },
            pan: { enabled: true, mode: 'xy' },
            zoom: {
              wheel: { enabled: true, speed: ZOOM_WHEEL_SPEED },
              pinch: { enabled: true },
              drag: { enabled: false },
              mode: 'xy',
              scaleMode: 'xy',
              maxScale: ZOOM_MAX_RATIO,
            },
          },
        },
        scales: {
          x: {
            type: 'linear',
            title: { display: true, text: 'x' },
            grid: { color: '#1f2733' },
            ticks: {
              callback: (val) => Number(val).toFixed(LABEL_PRECISION),
            },
          },
          y: {
            title: { display: true, text: 'y' },
            grid: { color: '#1f2733' },
            suggestedMin,
            suggestedMax,
            ticks: {
              callback: (val) => Number(val).toFixed(LABEL_PRECISION),
            },
          },
        },
        onClick: (evt, elements) => {
          if (onPointSelect && elements && elements.length > 0) {
            const el = elements[0];
            const xVal = this.chart.data.datasets[0].data[el.index]?.x;
            if (Number.isFinite(xVal)) onPointSelect(xVal);
          }
        },
      },
    });
  }
}


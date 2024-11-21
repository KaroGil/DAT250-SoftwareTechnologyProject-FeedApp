import { defineConfig } from 'vite'
import { svelte } from '@sveltejs/vite-plugin-svelte'
import { visualizer } from 'rollup-plugin-visualizer';


// https://vite.dev/config/
export default defineConfig({
  plugins: [svelte(),
    visualizer({
      open: true, // Automatically open the report in your browser
      filename: 'bundle-analysis.html', // Name of the report file
      template: 'treemap', // Visualization type: 'treemap', 'sunburst', etc.
    }),],
})

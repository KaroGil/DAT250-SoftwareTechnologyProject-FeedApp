import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { visualizer } from 'rollup-plugin-visualizer';


// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    visualizer({
      open: true, // Automatically open the report in your browser
      filename: 'bundle-analysis.html', // Name of the report file
      template: 'treemap', // Visualization type: 'treemap', 'sunburst', etc.
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})

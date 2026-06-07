import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  base: '/user-manager/',
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/user-manager/api': {
        target: 'http://localhost:8080',
        rewrite: (path) => path.replace(/^\/user-manager/, ''),
      },
    },
  },
  build: {
    outDir: 'dist',
    lib: {
      entry: 'src/parcel.tsx',
      formats: ['es'],
      fileName: () => 'assets/index.js',
    },
    rollupOptions: {
      output: {
        assetFileNames: 'assets/style[extname]',
      },
    },
  },
  define: {
    'process.env.NODE_ENV': JSON.stringify('production'),
  },
  test: {
    reporters: ['default'],
  },
})

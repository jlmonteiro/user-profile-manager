import { StrictMode } from 'react'
import { createRoot, Root } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { MantineProvider, createTheme } from '@mantine/core'
import { Notifications } from '@mantine/notifications'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import '@mantine/core/styles.css'
import '@mantine/notifications/styles.css'
import { App } from './App'

const theme = createTheme({
  primaryColor: 'blue',
  defaultRadius: 'md',
})

let root: Root | null = null
let queryClient: QueryClient | null = null

/** One-time initialization. */
export const bootstrap = async () => {}

/** Mount the application into the given container. */
export const mount = async (container: HTMLElement) => {
  queryClient = new QueryClient()
  root = createRoot(container)
  root.render(
    <StrictMode>
      <QueryClientProvider client={queryClient}>
        <MantineProvider theme={theme} defaultColorScheme="light">
          <Notifications />
          <BrowserRouter basename="/user-manager">
            <App embedded />
          </BrowserRouter>
        </MantineProvider>
      </QueryClientProvider>
    </StrictMode>,
  )
}

/** Unmount the application and clean up. */
export const unmount = async () => {
  root?.unmount()
  root = null
  queryClient = null
}

import { Routes, Route } from 'react-router-dom'
import { Text } from '@mantine/core'

/** Props for the App component. */
interface AppProps {
  /** Whether the app is rendered inside the launcher shell. */
  embedded?: boolean
}

/** Root application component with routing. */
export const App = ({ embedded }: AppProps) => {
  return (
    <Routes>
      <Route index element={<Text>Profile Page (coming soon)</Text>} />
      <Route path="users" element={<Text>Users Page (coming soon)</Text>} />
      <Route path="users/:id" element={<Text>User Detail (coming soon)</Text>} />
      <Route path="roles" element={<Text>Roles Page (coming soon)</Text>} />
      <Route path="groups" element={<Text>Groups Page (coming soon)</Text>} />
    </Routes>
  )
}

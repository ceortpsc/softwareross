import React, { useEffect, useState } from 'react'
import Login from './Login'
import { getToken, logout, fetchAPI, getRoles } from './AuthService'
import AdminPanel from './AdminPanel'
import EditorPanel from './EditorPanel'
import ManagerPanel from './ManagerPanel'

export default function App() {
  const [health, setHealth] = useState(null)
  const [books, setBooks] = useState([])
  const [loggedIn, setLoggedIn] = useState(!!getToken())
  const [roles, setRoles] = useState(getRoles())
  const [view, setView] = useState(null)

  useEffect(() => {
    fetch('/api/health')
      .then(r => r.json())
      .then(setHealth)
      .catch(() => setHealth({ status: 'down' }))
  }, [])

  async function loadBooks(){
    try {
      const r = await fetchAPI('/api/books')
      const j = await r.json()
      setBooks(j)
    } catch(e){ console.error(e) }
  }

  function onLogin(){
    setLoggedIn(true)
    setRoles(getRoles())
    loadBooks()
  }

  function onLogout(){
    logout(); setLoggedIn(false); setRoles([]); setBooks([])
  }

  return (
    <div style={{ padding: 24, fontFamily: 'Arial, sans-serif' }}>
      <h1>Bookish</h1>
      <p>Backend status: {health ? health.status : 'loading...'}</p>

      {!loggedIn ? (
        <Login onLogin={onLogin} />
      ) : (
        <div>
          <div>Signed in. Roles: {roles.join(', ')}</div>
          <button onClick={onLogout}>Sign out</button>

          <h2>Books</h2>
          <button onClick={loadBooks}>Load books</button>
          <ul>
            {books.map(b => <li key={b.id}>{b.title} — {b.author}</li>)}
          </ul>

          <div style={{marginTop:20}}>
            {roles.includes('ROLE_ADMIN') && <button onClick={()=>setView('admin')}>Admin</button>}
            {roles.includes('ROLE_MANAGER') && <button onClick={()=>setView('manager')}>Manager</button>}
            {roles.includes('ROLE_EDITOR') && <button onClick={()=>setView('editor')}>Editor</button>}
          </div>

          {view === 'admin' && roles.includes('ROLE_ADMIN') && <AdminPanel />}
          {view === 'manager' && roles.includes('ROLE_MANAGER') && <ManagerPanel />}
          {view === 'editor' && roles.includes('ROLE_EDITOR') && <EditorPanel />}
        </div>
      )}
    </div>
  )
}

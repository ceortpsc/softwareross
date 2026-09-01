import React, { useEffect, useState } from 'react'
import Login from './Login'
import { getToken, logout, fetchAPI, getRoles } from './AuthService'

export default function App() {
  const [health, setHealth] = useState(null)
  const [books, setBooks] = useState([])
  const [loggedIn, setLoggedIn] = useState(!!getToken())
  const [roles, setRoles] = useState(getRoles())

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

          {roles.includes('ROLE_ADMIN') && (
            <div style={{marginTop:20}}>
              <h3>Admin area</h3>
              <p>Restricted admin tools would appear here.</p>
            </div>
          )}
        </div>
      )}
    </div>
  )
}

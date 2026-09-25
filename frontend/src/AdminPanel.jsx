import React, { useEffect, useState } from 'react'
import { fetchAPI } from './AuthService'

export default function AdminPanel(){
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(()=>{ load() },[])
  async function load(){ setLoading(true); try{ const r=await fetchAPI('/api/admin/users'); setUsers(await r.json()); }catch(e){console.error(e)} finally{setLoading(false)} }

  async function setPwd(id){
    const pwd = prompt('New password for user')
    if (!pwd) return
    await fetchAPI(`/api/admin/users/${id}/password`, { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ password: pwd }) })
    alert('password updated')
  }

  return (
    <div style={{padding:20}}>
      <h2>Admin</h2>
      {loading ? <div>Loading...</div> : (
        <table>
          <thead><tr><th>ID</th><th>Username</th><th>Roles</th><th>Actions</th></tr></thead>
          <tbody>
            {users.map(u => <tr key={u.id}><td>{u.id}</td><td>{u.username}</td><td>{u.roles?.join(', ')}</td><td><button onClick={()=>setPwd(u.id)}>Set password</button></td></tr>)}
          </tbody>
        </table>
      )}
    </div>
  )
}

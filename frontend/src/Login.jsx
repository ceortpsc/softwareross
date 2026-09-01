import React, { useState } from 'react'
import { login } from './AuthService'

export default function Login({ onLogin }){
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [err, setErr] = useState(null)

  async function submit(e){
    e.preventDefault(); setErr(null)
    try{
      await login(username, password)
      onLogin()
    }catch(e){ setErr(e.message) }
  }

  return (
    <div style={{padding:20}}>
      <h2>Sign in</h2>
      <form onSubmit={submit}>
        <div><input placeholder="username" value={username} onChange={e=>setUsername(e.target.value)} /></div>
        <div><input placeholder="password" type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
        <div><button type="submit">Sign in</button></div>
        {err && <div style={{color:'red'}}>{err}</div>}
      </form>
    </div>
  )
}

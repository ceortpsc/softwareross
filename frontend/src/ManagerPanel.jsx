import React, { useEffect, useState } from 'react'
import { fetchAPI } from './AuthService'

export default function ManagerPanel(){
  const [stats, setStats] = useState(null)
  useEffect(()=>{ fetchAPI('/api/manager/stats').then(r=>r.json()).then(setStats).catch(()=>setStats(null)) },[])
  return (
    <div style={{padding:20}}>
      <h2>Manager</h2>
      {stats ? <ul><li>Books: {stats.books}</li><li>Users: {stats.users}</li></ul> : <div>Loading...</div>}
    </div>
  )
}

import React, { useState } from 'react'
import { fetchAPI } from './AuthService'

export default function EditorPanel(){
  const [title, setTitle] = useState('')
  const [author, setAuthor] = useState('')

  async function submit(e){
    e.preventDefault()
    try{
      const r = await fetchAPI('/api/editor/books', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ title, author }) })
      const j = await r.json()
      alert('Created book id: '+j.id)
      setTitle(''); setAuthor('')
    }catch(e){ alert('error: '+e.message) }
  }

  return (
    <div style={{padding:20}}>
      <h2>Editor</h2>
      <form onSubmit={submit}>
        <div><input placeholder="Title" value={title} onChange={e=>setTitle(e.target.value)} /></div>
        <div><input placeholder="Author" value={author} onChange={e=>setAuthor(e.target.value)} /></div>
        <div><button type="submit">Create</button></div>
      </form>
    </div>
  )
}

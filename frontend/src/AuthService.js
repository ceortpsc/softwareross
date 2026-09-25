const API = '/api/auth'

export async function login(username, password) {
  const resp = await fetch(`${API}/login`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  })
  if (!resp.ok) throw new Error('invalid credentials')
  const j = await resp.json()
  localStorage.setItem('token', j.token)
  localStorage.setItem('roles', JSON.stringify(j.roles || []))
  return j
}

export function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('roles')
}

export function getToken() { return localStorage.getItem('token') }
export function getRoles() { try { return JSON.parse(localStorage.getItem('roles')||'[]') } catch { return [] } }

export async function fetchAPI(path, opts={}){
  const headers = opts.headers || {}
  const token = getToken()
  if (token) headers.Authorization = `Bearer ${token}`
  const r = await fetch(path, {...opts, headers})
  if (r.status===401) { logout(); throw new Error('unauthorized') }
  return r
}

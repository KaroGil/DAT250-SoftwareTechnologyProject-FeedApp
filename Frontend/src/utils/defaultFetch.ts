// A default fetch function that is used to fetch data from the backend.
export const defaultFetch = async (
  url: string,
  method: string,
  token?: string,
  body?: object,
) => {
  const response = await fetch('http://localhost:8080/api' + url, {
    method: method,
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token?.trim()}`,
    },
    body: JSON.stringify(body),
  })

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}, port: http://localhost:8080/api${url}`)
  }

  const contentType = response.headers.get('Content-Type')
  if (contentType && contentType.includes('application/json')) {
    return response.json()
  } else {
    return response
  }
}

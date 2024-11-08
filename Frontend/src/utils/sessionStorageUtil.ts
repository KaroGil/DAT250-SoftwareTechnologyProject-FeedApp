/**
 * This file is used for functions for handling the session storage.
 * This is made so it is easy to change the value of the key in one place.
 */

/**
 * Returns the users JWT token from the session storage
 * Works only if a user is logged in, else it will return null
 * */
export const getUserToken = (): string | undefined => {
  const token = sessionStorage.getItem('userToken')
  return token ? token : undefined
}

/**
 * Set the value of the userToken session storage
 * */
export const setUserToken = (value: string) => {
  if (value) {
    sessionStorage.setItem('userToken', value)
  }
}

/**
 * Deletes the value of the user token
 */
export const deleteUserToken = () => {
  sessionStorage.removeItem('userToken')
}

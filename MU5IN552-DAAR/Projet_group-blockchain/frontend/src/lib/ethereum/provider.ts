import { ethers } from 'ethers'

// const MAINNET_KEY = import.meta.env.VITE_APP_MAINNET_KEY
// const MAINNET_URL = `https://eth-mainnet.alchemyapi.io/v2`
// export const MAINNET_ENDPOINT = `${MAINNET_URL}/${MAINNET_KEY}`
// export const mainnet = new ethers.providers.JsonRpcProvider(MAINNET_ENDPOINT)

// const POLYGON_KEY = import.meta.env.VITE_APP_POLYGON_KEY
// const POLYGON_URL = `https://polygon-mainnet.g.alchemy.com/v2`
// export const POLYGON_ENDPOINT = `${POLYGON_URL}/${POLYGON_KEY}`
// export const polygon = new ethers.providers.JsonRpcProvider(POLYGON_ENDPOINT)

// const KOVAN_KEY = import.meta.env.VITE_APP_KOVAN_KEY
// const KOVAN_URL = `https://eth-kovan.alchemyapi.io/v2`
// export const KOVAN_ENDPOINT = `${KOVAN_URL}/${KOVAN_KEY}`
// export const kovan = new ethers.providers.JsonRpcProvider(KOVAN_ENDPOINT)

export const LOCAL_URL = 'http://localhost:8545'
export const local = new ethers.providers.JsonRpcProvider(LOCAL_URL)

export const fromEnvironment = () => {
  // if (import.meta.env.VITE_APP_STAGING_MATIC_USD) return kovan
  // if (import.meta.env.DEV) return local
  // return polygon
  return local
}

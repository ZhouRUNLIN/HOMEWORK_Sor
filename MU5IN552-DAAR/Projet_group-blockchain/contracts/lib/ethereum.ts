import { ethers } from 'ethers'
import * as ens from './ethereum/ens'
import * as providers from './ethereum/provider'
export * as mainnet from './ethereum/mainnet'
export * as account from './ethereum/account'

export type Wallet = 'metamask' | 'silent'

export type Details = {
  provider: ethers.providers.Provider
  signer?: ethers.providers.JsonRpcSigner
  account?: string
  name?: string
}

const metamask = async (requestAccounts = true): Promise<Details | null> => {
  const ethereum = (window as any).ethereum
  if (ethereum) {
    if (requestAccounts)
      await ethereum.request({ method: 'eth_requestAccounts' })
    const provider = new ethers.providers.Web3Provider(ethereum as any)
    const accounts = await provider.listAccounts()
    const account = accounts.length ? accounts[0] : undefined
    const signer = account ? provider.getSigner() : undefined
    const name = await ens.lookup(account ?? '')
    return { provider, signer, account, name }
  }
  return null
}

const silent = async (): Promise<Details> => {
  const ethereum = (window as any).ethereum
  if (ethereum) {
    const unlocked = await ethereum?._metamask?.isUnlocked?.()
    if (unlocked) return (await metamask(false))!
    const provider = new ethers.providers.Web3Provider(ethereum as any)
    return { provider }
  }
  const provider = providers.fromEnvironment()
  return { provider }
}

export const connect = async (provider: Wallet) => {
  switch (provider) {
    case 'metamask':
      return metamask()
    case 'silent':
      return silent()
    default:
      return null
  }
}

export const accountsChanged = (callback: (accounts: string[]) => void) => {
  const ethereum = (window as any).ethereum
  if (ethereum && ethereum.on) {
    ethereum.on('accountsChanged', callback)
    return () => ethereum.removeListener('accountsChanged', callback)
  } else {
    return () => {}
  }
}

export const chainChanged = (callback: (accounts: string[]) => void) => {
  const ethereum = (window as any).ethereum
  if (ethereum && ethereum.on) {
    ethereum.on('chainChanged', callback)
    return () => ethereum.removeListener('chainChanged', callback)
  } else {
    return () => {}
  }
}

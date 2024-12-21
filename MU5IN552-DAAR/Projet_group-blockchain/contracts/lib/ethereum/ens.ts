import { mainnet } from './provider'

export const resolve = async (name: string) => {
  if (!name.endsWith('.eth')) return null
  return await mainnet.resolveName(name)
}

export const lookup = async (address: string) => {
  if (!address.startsWith('0x')) return undefined
  const name = await mainnet.lookupAddress(address)
  return name ?? undefined
}

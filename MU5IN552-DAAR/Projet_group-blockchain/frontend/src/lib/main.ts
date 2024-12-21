import { ethers } from 'ethers'
import * as ethereum from './ethereum'
import { contracts } from '@/contracts.json'
// @ts-ignore
import type { Main } from '$/Main'
// @ts-ignore
export type { Main } from '$/Main'

export const correctChain = () => {
  return 31337
}

export const init = async (details: ethereum.Details) => {
  const { provider, signer } = details
  const network = await provider.getNetwork()
  if (correctChain() !== network.chainId) {
    console.error('Please switch to HardHat')
    return null
  }
  const { address, abi } = contracts.Main
  const contract = new ethers.Contract(address, abi, provider)
  const deployed = await contract.deployed()
  if (!deployed) return null
  const contract_ = signer ? contract.connect(signer) : contract
  return contract_ as any as Main
}

export const myShip = () => contracts.Main.address

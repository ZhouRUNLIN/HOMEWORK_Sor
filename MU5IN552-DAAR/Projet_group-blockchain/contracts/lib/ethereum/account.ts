export const compact = (account?: string) => {
  const acc = account ?? ''
  const start = acc.slice(0, 7)
  const length = acc.length
  const end = acc.slice(length - 5, length)
  return `${start}...${end}`
}

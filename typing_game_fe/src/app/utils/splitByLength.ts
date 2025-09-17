export const splitByLength = (text: string, maxLength: number): string[] => {
  const rawLines = text.split("\n");
  const result: string[] = [];

  rawLines.forEach(line => {
    const cleanLine = line.replace(/\r/g, "").replace(/\s+$/g, "");

    if (cleanLine.length <= maxLength) {
      result.push(cleanLine);
    } else {
      for (let i = 0; i < cleanLine.length; i += maxLength) {
        const segment = cleanLine.slice(i, i + maxLength).replace(/\s+$/g, "");
        result.push(segment);
      }
    }
  });

  return result;
};

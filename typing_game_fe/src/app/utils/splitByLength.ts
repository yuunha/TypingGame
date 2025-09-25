export const splitByLength = (text: string, maxLength: number): string[] => {
  
  const lines = text.split(/\n|(?<=다\.)|(?<=\.\")|(?<=\?\")/g).map(line=>line.trim()).filter(line => line.length > 0);
  const result: string[] = [];

  lines.forEach(line => {
    if (line.length <= maxLength) {
      result.push(line);
    }
    else {
      for (let i = 0; i < line.length; i += maxLength) {
        const segment = line.slice(i, i + maxLength).trim();
        result.push(segment);
      }
    }
  });

  return result;
};

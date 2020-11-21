const httpRequest = async (url, options = {}) => {
  Object.assign(options, {
    headers: {
      'Accept': 'application/vnd.github.v3+json',
      'Authorization': 'token  6dac0a9d5dffdbcbc95afb1be37b579e98ccf8a6',
    },
  });

  const res = await fetch(url, options);

  // TODO handle error codes?
  if (res.status === 204 || res.status === 205) {
    return null;
  }
  return await res.json();
};
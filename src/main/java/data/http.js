const httpRequest = async (url, options = {}) => {
  Object.assign(options, {
    headers: {
      'Accept': 'application/vnd.github.v3+json',
    },
  });

  const res = await fetch(url, options);

  // TODO handle error codes?
  if (res.status === 204 || res.status === 205) {
    return null;
  }
  return await res.json();
};
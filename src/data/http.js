const httpRequest = async (url, options = {}) => {
  Object.assign(options, {
    headers: {
      'Accept': 'application/vnd.github.v3+json',
    },
  });

  const res = await fetch(url, options);

  // TODO handle error codes?
  return await res.json();
};
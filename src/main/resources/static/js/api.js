export class Api {
  constructor(baseUrl = '/api') {
    this.baseUrl = baseUrl;
    this.auth = null;
  }

  setAuth(username, password) {
    if (username && password) {
      this.auth = btoa(`${username}:${password}`);
    }
  }

  clearAuth() {
    this.auth = null;
  }

  async request(method, path, body, extraHeaders = {}) {
    const headers = {
      'Content-Type': 'application/json',
      ...extraHeaders,
    };

    if (this.auth) {
      headers.Authorization = `Basic ${this.auth}`;
    }

    const response = await fetch(`${this.baseUrl}${path}`, {
      method,
      headers,
      body: body ? JSON.stringify(body) : undefined,
      cache: 'no-store',
    });

    let payload = null;
    try {
      payload = await response.json();
    } catch (e) {
      // ignore json parse issues/ handled later -Dasha
    }

    const success = response.ok && (payload?.success ?? true);
    if (!success) {
      const message = payload?.message || response.statusText || 'Неизвестная ошибка';
      throw new Error(message);
    }

    return payload?.data ?? payload;
  }

  get(path) {
    return this.request('GET', path);
  }

  post(path, body) {
    return this.request('POST', path, body);
  }

  put(path, body) {
    return this.request('PUT', path, body);
  }

  delete(path) {
    return this.request('DELETE', path);
  }
}


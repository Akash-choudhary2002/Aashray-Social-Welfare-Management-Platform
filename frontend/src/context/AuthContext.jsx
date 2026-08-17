import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import authApi from '../api/authApi';

const AuthContext = createContext(null);

const TOKEN_KEY = 'aashray_token';
const USER_KEY = 'aashray_user';

function readStoredUser() {
  try {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(readStoredUser());
  const [token, setToken] = useState(localStorage.getItem(TOKEN_KEY));
  const [initializing, setInitializing] = useState(true);

  // "Auto login": if a token is already in storage on app load, trust it
  // and refresh the profile in the background so stale role/name info
  // (e.g. after an admin edit) gets corrected.
  useEffect(() => {
    async function bootstrap() {
      if (localStorage.getItem(TOKEN_KEY)) {
        try {
          const profile = await authApi.getProfile();
          const merged = { ...readStoredUser(), ...profile.data };
          setUser(merged);
          localStorage.setItem(USER_KEY, JSON.stringify(merged));
        } catch {
          // token invalid/expired — axiosInstance interceptor already
          // clears storage and redirects to /login on a 401.
        }
      }
      setInitializing(false);
    }
    bootstrap();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const login = useCallback(async (email, password) => {
    const response = await authApi.login({ email, password });
    const authData = response.data;
    localStorage.setItem(TOKEN_KEY, authData.accessToken);
    const storedUser = {
      id: authData.userId,
      fullName: authData.fullName,
      email: authData.email,
      role: authData.role
    };
    localStorage.setItem(USER_KEY, JSON.stringify(storedUser));
    setToken(authData.accessToken);
    setUser(storedUser);
    return storedUser;
  }, []);

  const register = useCallback((payload) => authApi.register(payload), []);

  const logout = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    setToken(null);
    setUser(null);
  }, []);

  const refreshProfile = useCallback(async () => {
    const profile = await authApi.getProfile();
    const merged = { ...readStoredUser(), ...profile.data };
    setUser(merged);
    localStorage.setItem(USER_KEY, JSON.stringify(merged));
    return merged;
  }, []);

  const hasRole = useCallback((...roles) => !!user && roles.includes(user.role), [user]);

  const value = useMemo(
    () => ({
      user,
      token,
      isAuthenticated: !!token,
      initializing,
      login,
      register,
      logout,
      refreshProfile,
      hasRole
    }),
    [user, token, initializing, login, register, logout, refreshProfile, hasRole]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return ctx;
}

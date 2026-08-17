import { Component } from 'react';
import ErrorPage from '../../pages/common/ErrorPage';

export default class ErrorBoundary extends Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError() {
    return { hasError: true };
  }

  componentDidCatch(error, info) {
    // eslint-disable-next-line no-console
    console.error('Aashray frontend crashed:', error, info);
  }

  render() {
    if (this.state.hasError) {
      return <ErrorPage message="This page ran into an unexpected error. Please refresh." />;
    }
    return this.props.children;
  }
}

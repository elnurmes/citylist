import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useHistory } from "react-router-dom"; 

function Login({isAuthenticated, setIsAuthenticated}) {
	const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  let history = useHistory();

  function timeout(delay) {
    return new Promise( res => setTimeout(res, delay) );
  }

	const onSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8558/v1/auth/login', {email, password});
      sessionStorage.setItem('token', response.data.token);
      sessionStorage.setItem('email', response.data.email);
      setIsAuthenticated(true);
    } catch(error){
      setMessage('');
      if (error.response) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage('Error: something happened');
      }
      setIsAuthenticated(false);
      return;
    }
    
    setEmail('');
    setPassword('');
    setErrorMessage('');
    setMessage('Sign in successful');
    await timeout(1000);
    history.push("/");
  }

  useEffect(() => {
    setMessage('')
  }, [email, password])

  const showMessage = () => {
    if(message === ''){
      return <div></div>
    }
    return <div className="alert alert-success" role="alert">
      {message}
    </div> 
  }

  const showErrorMessage = () => {
    if(errorMessage === ''){
      return <div></div>
    }

    return <div className="alert alert-danger" role="alert">
      {errorMessage}
    </div>
  }

	return (
		<div className="container">
      <form onSubmit={onSubmit}>
        <h1>Sign In</h1>
        <div className="form-group">
          <label>Username</label>
          <input 
            value={email} 
            onChange={e => setEmail(e.target.value)} 
            placeholder="Username"
            className="form-control">
          </input>
        </div>
        <div className="form-group">
          <label>Password</label>
          <input 
            value={password} 
            type="password" 
            onChange={e => setPassword(e.target.value)}
            placeholder="Password"
            className="form-control">
          </input>
        </div>
        <button className="btn btn-primary">Sign In</button>
      </form>
      {showMessage()}
      {showErrorMessage()}
    </div>
	)
}

export default Login;
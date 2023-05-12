import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useHistory } from "react-router-dom"; 

function EditCity({isAuthenticated, setIsAuthenticated, match}) {
	const [name, setName] = useState('');
  const [photo, setPhoto] = useState('');
  const [message, setMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  let history = useHistory();

  useEffect(() => {
		if(!isAuthenticated){
			history.push("/");
		}
	}, [isAuthenticated, history])

  function timeout(delay) {
    return new Promise( res => setTimeout(res, delay) );
  }

  const onSubmit = async (e) => {
    e.preventDefault();
  
    try {
      await axios({
        method: 'put',
        url: 'http://localhost:8558/v1/city/',
        data: {
            id:    match.params.id,
            name: name,
            photo: photo
        },
        headers: {'Authorization': `Bearer ${sessionStorage.getItem('token')}`}
    });
    } catch(error){
      setMessage('');
      if (error.response) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage('Error: something happened');
      }
      return;
    }

    setErrorMessage('');
    setMessage('City successfully updated');
    await timeout(1000);
    history.push("/");
  }

  useEffect(() => {
    const loadData = async () => {
      let response = null;
      try {
        response = await axios.get(`http://localhost:8558/v1/city/id/${match.params.id}`, {
        });
      } catch(error){
        setMessage('');
        if (error.response) {
          setErrorMessage(error.response.data.message);
        } else {
          setErrorMessage('Error: something happened');
        }
        return;
      }
      setErrorMessage('');
      setName(response.data.name);
      setPhoto(response.data.photo);
    }
    
		loadData();
  }, [match.params.id]);

  useEffect(() => {
    setMessage('')
  }, [name, photo])
  
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
        <h1>Edit City</h1>
        <div className="form-group">
          <label>Name</label>
          <input 
            value={name} 
            onChange={e => setName(e.target.value)} 
            className="form-control">
          </input>
        </div>
        <div className="form-group">
          <label>Photo</label>
          <input 
            value={photo}
            onChange={e => setPhoto(e.target.value)} 
            className="form-control">
          </input>
        </div>
        <button className="btn btn-primary">Edit</button>
      </form>
      {showMessage()}
      {showErrorMessage()}
    </div>
	)
}

export default EditCity;
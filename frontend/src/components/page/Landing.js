import React, { useEffect, useState } from 'react';

export default function Landing({setIsAuthenticated}) {
  const [message, setMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('');

  const showErrorMessage = () => {
    if(errorMessage === ''){
      return <div></div>
    }

    return <div className="alert alert-danger" role="alert">
      {errorMessage}
    </div>
  }

	return (
		<div className="text-center">
			<h1>City List Application</h1>
      {showErrorMessage()}
			{message}
		</div>
	)
}
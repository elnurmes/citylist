import React, { useState, useEffect } from 'react';
import axios from 'axios';
import placeholderSrc from '../../loading-gif.gif'
import ProgressiveImage from 'react-progressive-image';
import { Link } from 'react-router-dom';
import { Modal } from 'react-bootstrap';

function Cities({isAuthenticated, setIsAuthenticated}) {
 const [cities, setCities] = useState([]);
 const [changed, setChanged] = useState(false);
 const [errorMessage, setErrorMessage] = useState('');
 const [pageNumber, setPageNumber] = useState(1);
 const [pageSize, setPageSize] = useState(5);
 const [inputPageNumber, setInputPageNumber] = useState(pageNumber);
 const [inputPageSize, setInputPageSize] = useState(pageSize);
 const [searchQuery, setSearchQuery] = useState('');
 const [modalOpen, setModalOpen] = useState(false);
const [modalImage, setModalImage] = useState(null);

 useEffect(() => {
const loadData = async () => {
  let response = null;
  try {
    let url = '';
    if (searchQuery !== '') {
      url = `http://localhost:8558/v1/city/${searchQuery}`;
    } else {
      url = `http://localhost:8558/v1/city/all?pageNo=${pageNumber - 1}&pageSize=${pageSize}`;
    }

    response = await axios.get(url);
  } catch(error){
    if (error.response) {
      setErrorMessage(error.response.data.message);
    } else {
      setErrorMessage('Error: dd happened');
    }
    return;
  }
  setErrorMessage('');
  setCities(response.data);
}

  loadData();
 }, [changed, pageNumber, pageSize])

 const searchCityByName = async (name) => {
	try {
	  const response = await axios.get(`http://localhost:8558/v1/city/${name}`);
	  setCities(response.data);
	} catch(error){
	  if (error.response) {
		setErrorMessage(error.response.data.message);
	  } else {
		setErrorMessage('Error: dd happened');
	  }
	}
}

const handleRowClick = (image) => {
	setModalOpen(true);
	setModalImage(image);
  }

 const nextPage = () => {
  setPageNumber(pageNumber + 1);
  setInputPageNumber(pageNumber + 1);
 }

 const previousPage = () => {
  if(pageNumber > 1){
   setPageNumber(pageNumber - 1);
   setInputPageNumber(pageNumber - 1);
  }
 }
  
	const enterPageNumber = (enteredPageNumber) => {
		if(enteredPageNumber >= 1){
			setPageNumber(parseInt(enteredPageNumber));
		} else {
			setPageNumber(1);
			setInputPageNumber(1);
		}
	}

	const enterPageSize = (enteredPageSize) => {
		if(enteredPageSize >= 1){
			setPageSize(parseInt(enteredPageSize));
		} else {
			setPageSize(1);
			setInputPageSize(1);
		}
	}

	const pageNumberControl = () => {
		return <div>
			<center>
				<div className="input-group col-lg-4 col-md-6 col-sm-8 col-9">
					<div className="input-group-append">
						<button className="btn btn-outline-secondary" onClick={() => previousPage()} >Previous Page</button>
					</div>
					<input className="form-control text-center" type="number" value={inputPageNumber} onChange={e => setInputPageNumber(e.target.value)} onKeyPress={e => {
									if (e.key === 'Enter') {
										enterPageNumber(e.target.value)
									}
								}}/>
					<div className="input-group-append">
						<button className="btn btn-outline-secondary" onClick={() => nextPage()}>Next Page</button>
					</div>
				</div>
			</center>
		</div>
	}

	const pageSizeControl = () => {
		return <center>
			<div className="input-group col-xl-3 col-md-4 col-sm-5 col-6">
				<div className="input-group-append">
					<span className="input-group-text" id="">City per page: </span>
				</div>
				<input className="form-control text-center" type="number" value={inputPageSize} onChange={e => setInputPageSize(e.target.value)} onKeyPress={e => {
								if (e.key === 'Enter') {
									enterPageSize(e.target.value)
								}
							}}
				/>
			</div>
		</center>
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
	  <h1 className="text-center">City List</h1>
	  {showErrorMessage()}
	  <div class="search-container">
  		<input class="search-input" type="text" value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)} placeholder="Search by name" />
  		<button class="search-button" onClick={() => searchCityByName(searchQuery)}>Search</button>
	</div>
  
	  <table className="table">
		<thead>
		  <tr>
			<th>id</th>
			<th>name</th>
			<th>photo</th>
			 {isAuthenticated && <th>Edit</th>}
		  </tr>
		</thead>
		<tbody>
		  {cities.map((city) => {
			return (
				<tr key={city.id} onClick={() => handleRowClick(city.photo)} style={{cursor: 'pointer'}}>
				<td>{city.id}</td>
				<td>{city.name}</td>
				<td>
				  <ProgressiveImage src={city.photo} placeholder={placeholderSrc}>
					{(src, loading) => (
					  <img
						className={`image${loading ? " loading" : " loaded"}`}
						src={src}
						alt="sea beach"
						width="50"
						height="50"
					  />
					)}
				  </ProgressiveImage>
				</td>
				<td>
				{isAuthenticated && (
				    <td>
					<Link to={{ pathname: `/edit/${city.id}` }}>
					  <button className="btn btn-primary">Edit</button>
					</Link>
					</td>)
					}
				</td>
			  </tr>
			);
		  })}
		</tbody>
	  </table>
	  <Modal show={modalOpen} 
	        //dialogClassName="modal-90w"
	  		onHide={() => setModalOpen(false)}>
		
      <Modal.Body>
	  <span className="close" onClick={() => setModalOpen(false)}>
          &times;
        </span>
        <ProgressiveImage src={modalImage} placeholder={placeholderSrc}>
					{(src, loading) => (
					  <img
						className={`image${loading ? " loading" : " loaded"}`}
						src={src}
						width="90%"
						height="90%"
					  />
					)}
				  </ProgressiveImage>
      </Modal.Body>
    </Modal>
	  {pageSizeControl()}
	  {pageNumberControl()}
	</div>
  );
}

export default Cities;
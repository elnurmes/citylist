import React, { useEffect , useState} from 'react';
import { useHistory } from "react-router-dom";
import { Modal, Button } from 'react-bootstrap';

function Logout({isAuthenticated, setIsAuthenticated}) {
  let history = useHistory();
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    setShowModal(true);
  }, [setIsAuthenticated])

  const handleClose = () => {
	sessionStorage.removeItem('token');
	sessionStorage.removeItem('name');
	setIsAuthenticated(false);
    setShowModal(false);
    history.push("/");
  }

  return (
    <div className="text-center">
      <h1>Successfully sign out</h1>

      <Modal show={showModal} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Confirmation</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>Are you sure you want to sign out?</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleClose}>
            Sign out
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  )
}

export default Logout;
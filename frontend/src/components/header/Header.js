import React from 'react';
import { Link } from 'react-router-dom';

function Header({isAuthenticated, setIsAuthenticated}) {
	return (
		<header>
			<nav className="navbar navbar-expand-md navbar-dark bg-dark sticky-top">
        <div className="navbar-brand container">CityList App</div>
        <ul className="navbar-nav justify-content-end container">
          <li className="nav-link px-4"><Link to='/'>Home</Link></li>
          {!isAuthenticated && <li className="nav-link px-4"><Link to='/login'>Login</Link></li>}
          {isAuthenticated && <li className="nav-link px-4"><Link to='/logout'>Logout</Link></li>}
          <li className="nav-link px-4"><Link to='/about'>About</Link></li>
        </ul>
      </nav>
		</header>
	)
}

export default Header;
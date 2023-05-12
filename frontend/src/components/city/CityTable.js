import React, { useState } from "react";
import Modal from "./Modal"; // import your Modal component here

function CityTable({ cities, isAuthenticated }) {
  const [selectedCity, setSelectedCity] = useState(null);

  const handleRowClick = (city) => {
    setSelectedCity(city);
  };

  const handleCloseModal = () => {
    setSelectedCity(null);
  };

  return (
    <div>
      <table className="table">
        <thead>
          <tr>
            <th>id</th>
            <th>name</th>
            <th>photo</th>
          </tr>
        </thead>
        <tbody>
          {cities.map((city) => {
            return (
              <tr key={city.id} onClick={() => handleRowClick(city)}>
                <td>{city.id}</td>
                <td>{city.name}</td>
                <td>
                  <ProgressiveImage
                    src={city.photo}
                    placeholder={placeholderSrc}
                  >
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
                {isAuthenticated && (
                  <td>
                    <Link to={{ pathname: `/edit/${city.id}` }}>
                      <button className="btn btn-primary">Edit</button>
                    </Link>
                  </td>
                )}
              </tr>
            );
          })}
        </tbody>
      </table>
      {selectedCity && (
        <Modal onClose={handleCloseModal}>
          <img src={selectedCity.photo} alt={selectedCity.name} />
        </Modal>
      )}
    </div>
  );
}

export default CityTable;
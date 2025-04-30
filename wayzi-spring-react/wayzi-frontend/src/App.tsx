import './App.css'
import './styles/global.css'
import './styles/forms.css'
import {BrowserRouter} from "react-router-dom";
import RoutesConfig from "./auth/RoutesConfig.tsx";
import {UserProvider} from "./context/UserContext.tsx";
import Header from "./components/Header.tsx";


function App() {

  return (
    <>
        <UserProvider>
            <BrowserRouter>
                <Header />
                <div className="app">
                    <RoutesConfig />
                </div>
            </BrowserRouter>
        </UserProvider>
    </>
  )
}

export default App

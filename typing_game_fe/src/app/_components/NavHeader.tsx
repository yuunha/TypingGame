'use client';

import styled from "styled-components";
import Link from "next/link"; 

const NavHeader: React.FC= () => {

    return(
        <NavContainer>
            <NavBarInner>
                <NavBarLogo> 
                    <Link href="/">TYLE</Link>
                </NavBarLogo>
                <div>
                    <NavLink href="/login">Account</NavLink>
                    <NavLink href="/rank">Rank</NavLink>
                    <NavLink href="/profile">Profile</NavLink>
                </div>
            </NavBarInner>
        </NavContainer>
    );
};


export default NavHeader;

const NavContainer = styled.nav`
    width:100%;
    margin-bottom : 50px;
    display: flex;
    align-items:center;
    justify-content: center;
    font-size : var(--tpg-header-font-size);
`

const NavBarInner = styled.div`
  width: var(--tpg-basic-width);
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
;
`
const NavBarLogo = styled.div`
    font-family: Libertinus, Helvetica, sans-serif;
    font-weight : bold;
;
`
const NavLink = styled(Link)`
    font-family: Montserrat, Helvetica, sans-serif;
  margin-left: 12px;
`;
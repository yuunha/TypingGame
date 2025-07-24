import React from 'react';
import styled from 'styled-components';

interface WidgetProps {
  userName?: string; 
}
const WidgetWrapper = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 400px;
  color: white;
`;

const IconBar = styled.div`
  width: 4px;
  height: 45px;
  border-radius: 3px;
  background-color: white;
  margin-right: 5px;
`;

const TextContent = styled.div`
  flex-direction: column;
  justify-content: center;
  font-size: 0.7rem;
`;

const Widget: React.FC<WidgetProps> = ({ userName }) => {
  const today = new Date();

  const formattedDate = today.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  return (
    <WidgetWrapper>
      <IconBar />
      <TextContent>
        {formattedDate}<br/>
        한컴타자연습<br/>
        {userName ? userName : '비회원'}
      </TextContent>
    </WidgetWrapper>
  );
};

export default Widget;

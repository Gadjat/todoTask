FROM maven

WORKDIR /todo
COPY . .
RUN mvn clean package -DskipTests

CMD mvn spring-boot:run
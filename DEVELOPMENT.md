# Development Guide

This guide covers development workflows, coding standards, and best practices for the AI E-Commerce Backend project.

## Table of Contents

- [Project Structure](#project-structure)
- [Technology Stack](#technology-stack)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [API Development](#api-development)
- [Testing Guidelines](#testing-guidelines)
- [Database Management](#database-management)
- [Security Best Practices](#security-best-practices)
- [Performance Optimization](#performance-optimization)
- [Troubleshooting](#troubleshooting)

## Project Structure

```
ai-ecommerce-backend/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── exception/        # Custom exceptions
│   │   │   ├── model/            # Entity classes
│   │   │   ├── repository/       # JPA repositories
│   │   │   ├── service/          # Business logic
│   │   │   ├── security/         # Security configuration
│   │   │   └── util/             # Utility classes
│   │   └── resources/
│   │       ├── application.yml   # Configuration
│   │       └── static/           # Static resources
│   └── test/
│       └── java/com/ecommerce/   # Test classes
├── database/                      # Database scripts
├── docker-compose.yml            # Docker configuration
├── Dockerfile.backend            # Backend Docker image
├── pom.xml                       # Maven configuration
└── README.md
```

## Technology Stack

### Core Technologies

- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database access
- **PostgreSQL 15** - Relational database
- **Redis** - Caching layer
- **RabbitMQ** - Message broker
- **Maven** - Build tool
- **Docker** - Containerization

### Libraries

- **Lombok** - Reduce boilerplate code
- **JWT (jjwt)** - Token-based authentication
- **Jackson** - JSON processing
- **Hibernate** - ORM framework
- **Jedis** - Redis client

## Development Workflow

### 1. Feature Development

```bash
# Create feature branch
git checkout -b feature/new-feature

# Make changes and test locally
mvn clean test

# Commit changes
git add .
git commit -m "feat: Add new feature"

# Push to remote
git push origin feature/new-feature

# Create Pull Request
```

### 2. Running Locally

#### With Docker (Recommended)

```bash
# Start all services
docker compose up -d

# View logs
docker compose logs -f backend

# Stop services
docker compose down
```

#### Without Docker

```bash
# Ensure PostgreSQL, Redis, and RabbitMQ are running

# Run application
mvn spring-boot:run

# Or with custom profile
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### 3. Testing Changes

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Run specific test
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn clean test jacoco:report
```

### 4. Building for Production

```bash
# Build JAR
mvn clean package -DskipTests

# Build Docker image
docker build -f Dockerfile.backend -t ai-ecommerce-backend .

# Run Docker container
docker run -p 9090:9090 ai-ecommerce-backend
```

## Coding Standards

### Java Conventions

1. **Naming Conventions**:
   - Classes: PascalCase (e.g., `UserService`)
   - Methods/Variables: camelCase (e.g., `getUserById`)
   - Constants: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_ATTEMPTS`)
   - Packages: lowercase (e.g., `com.ecommerce.service`)

2. **Code Organization**:
   - One public class per file
   - Maximum 500 lines per class
   - Maximum 50 lines per method
   - Use meaningful names

3. **Lombok Usage**:
   ```java
   @Data                    // Getters, setters, toString, equals, hashCode
   @NoArgsConstructor       // Default constructor
   @AllArgsConstructor      // Constructor with all fields
   @Builder                 // Builder pattern
   @Slf4j                   // Logger
   ```

### REST API Conventions

1. **HTTP Methods**:
   - GET: Retrieve resources
   - POST: Create resources
   - PUT: Update entire resources
   - PATCH: Partial updates
   - DELETE: Remove resources

2. **URL Structure**:
   ```
   GET    /api/products              # List products
   GET    /api/products/{id}         # Get product
   POST   /api/products              # Create product
   PUT    /api/products/{id}         # Update product
   DELETE /api/products/{id}         # Delete product
   GET    /api/products/search       # Search products
   ```

3. **Response Format**:
   ```json
   {
     "success": true,
     "data": { ... },
     "message": "Operation successful",
     "timestamp": "2024-12-31T00:00:00Z"
   }
   ```

### Exception Handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
        ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

## API Development

### Creating a New Endpoint

1. **Define DTO**:
```java
@Data
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
}
```

2. **Create Controller**:
```java
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.info("Fetching product with id: {}", id);
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
}
```

3. **Implement Service**:
```java
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Product not found with id: " + id));
        return mapToDTO(product);
    }
}
```

### Request Validation

```java
@Data
public class CreateProductRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 255)
    private String name;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    private BigDecimal price;
    
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    @Email(message = "Invalid email format")
    private String contactEmail;
}
```

### Authentication & Authorization

```java
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        // Only admins with VIEW_USERS permission can access
    }
}
```

## Testing Guidelines

### Unit Tests

```java
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductServiceImpl productService;
    
    @Test
    void testGetProductById_Success() {
        // Arrange
        Long productId = 1L;
        Product product = Product.builder()
            .id(productId)
            .name("Test Product")
            .price(new BigDecimal("99.99"))
            .build();
        
        when(productRepository.findById(productId))
            .thenReturn(Optional.of(product));
        
        // Act
        ProductDTO result = productService.getProductById(productId);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(productId);
    }
    
    @Test
    void testGetProductById_NotFound() {
        // Arrange
        Long productId = 999L;
        when(productRepository.findById(productId))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
            () -> productService.getProductById(productId));
    }
}
```

### Integration Tests

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
class ProductControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Test
    void testCreateProduct() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Test Product");
        request.setPrice(new BigDecimal("99.99"));
        
        // Act
        ResponseEntity<ProductDTO> response = restTemplate
            .postForEntity("/api/products", request, ProductDTO.class);
        
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }
}
```

## Database Management

### Entity Design

```java
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### Repository Pattern

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    );
    
    Optional<Product> findBySku(String sku);
}
```

### Database Migrations

Use Flyway or Liquibase for database versioning:

```sql
-- V1__create_products_table.sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Security Best Practices

### 1. Authentication

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, 
                UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

### 2. Password Handling

```java
@Service
public class UserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User createUser(RegisterRequest request) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }
}
```

### 3. Input Validation

- Always validate user input
- Use Bean Validation annotations
- Sanitize data before database operations
- Prevent SQL injection (use JPA/Hibernate)
- Protect against XSS attacks

### 4. API Security

- Use HTTPS in production
- Implement rate limiting
- Add CORS configuration
- Use JWT with short expiration
- Implement refresh tokens

## Performance Optimization

### 1. Caching

```java
@Service
@CacheConfig(cacheNames = "products")
public class ProductService {
    
    @Cacheable(key = "#id")
    public ProductDTO getProductById(Long id) {
        // Cached result
    }
    
    @CacheEvict(key = "#id")
    public void deleteProduct(Long id) {
        // Evict cache
    }
    
    @CachePut(key = "#result.id")
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        // Update cache
    }
}
```

### 2. Database Optimization

```java
@Entity
@Table(indexes = {
    @Index(name = "idx_product_name", columnList = "name"),
    @Index(name = "idx_product_sku", columnList = "sku")
})
public class Product {
    // Indexed fields for faster queries
}
```

### 3. Lazy Loading

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;
```

### 4. Pagination

```java
@GetMapping
public Page<ProductDTO> getProducts(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "id,desc") String sort) {
    
    Pageable pageable = PageRequest.of(page, size, 
        Sort.by(Sort.Direction.fromString(sort.split(",")[1]),
                sort.split(",")[0]));
    
    return productService.findAll(pageable);
}
```

## Troubleshooting

### Common Issues

1. **Connection Pool Exhausted**:
   - Increase pool size in `application.yml`
   - Check for connection leaks
   - Use `@Transactional` properly

2. **N+1 Query Problem**:
   - Use `@EntityGraph`
   - Use JOIN FETCH in queries
   - Enable batch fetching

3. **Memory Issues**:
   - Increase JVM heap size: `-Xmx2g`
   - Use pagination for large datasets
   - Profile with JProfiler or VisualVM

4. **Slow Queries**:
   - Add database indexes
   - Use EXPLAIN ANALYZE
   - Optimize JOIN operations
   - Consider denormalization

## Git Workflow

### Commit Messages

Follow conventional commits:

```
feat: Add user registration endpoint
fix: Resolve null pointer exception in ProductService
docs: Update API documentation
test: Add unit tests for OrderService
refactor: Simplify cart calculation logic
perf: Optimize product search query
```

### Branch Naming

```
feature/user-authentication
bugfix/cart-calculation-error
hotfix/security-vulnerability
release/v1.0.0
```

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JPA Best Practices](https://thorben-janssen.com/tips-to-boost-your-hibernate-performance/)
- [REST API Design](https://restfulapi.net/)
- [Clean Code Principles](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)

## Support

For questions or issues:
- Create an issue on GitHub
- Check existing documentation
- Review closed issues and PRs

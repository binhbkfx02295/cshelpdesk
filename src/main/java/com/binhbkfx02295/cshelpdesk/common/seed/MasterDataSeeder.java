package com.binhbkfx02295.cshelpdesk.common.seed;

import com.binhbkfx02295.cshelpdesk.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Status;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.StatusRepository;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.service.EmployeeServiceImpl;
import com.binhbkfx02295.cshelpdesk.employee_management.permission.Permission;
import com.binhbkfx02295.cshelpdesk.employee_management.permission.PermissionRepository;
import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroup;
import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroupRepository;
import com.binhbkfx02295.cshelpdesk.facebookuser.entity.FacebookUser;
import com.binhbkfx02295.cshelpdesk.facebookuser.repository.FacebookUserRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.entity.Category;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.repository.CategoryRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.emotion.entity.Emotion;
import com.binhbkfx02295.cshelpdesk.ticket_management.satisfaction.entity.Satisfaction;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.entity.ProgressStatus;
import com.binhbkfx02295.cshelpdesk.ticket_management.emotion.repository.EmotionRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.satisfaction.repository.SatisfactionRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.repository.ProgressStatusRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.entity.Ticket;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

import com.github.javafaker.Faker;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class MasterDataSeeder implements CommandLineRunner {

    private final ProgressStatusRepository progressStatusRepository;
    private final EmotionRepository emotionRepository;
    private final SatisfactionRepository satisfactionRepository;
    private final EmployeeServiceImpl employeeService;
    private final StatusRepository  statusRepository;
    private final UserGroupRepository userGroupRepository;
    private final PermissionRepository permissionRepository;
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final MasterDataCache cache;
    private final FacebookUserRepository facebookUserRepository;
    private final TicketRepository ticketRepository;


    @Override
    public void run(String... args) {
        seedPermission();
        seedUserGroup();
        seedStatus();
        seedEmployee();
        seedProgressStatuses();
        seedCustomerEmotions();
        seedCustomerSatisfactions();
        seedCategory();
        seedFacebookUsers();
        seedFacebookUsers(50);
        seedTickets(50);

        log.info("seeding done");
        cache.refresh();
    }

    private void seedTickets() {
    }

    private void seedFacebookUsers() {
        FacebookUser user = new FacebookUser();
        user.setFacebookId("9788775675676567");
        user.setFacebookName("Le Nguyen Minh Huy");
        user.setFacebookProfilePic("/img/placeholder-facebook-1.jpg");
        if (!facebookUserRepository.existsById("9788775675676567")) {
            facebookUserRepository.save(user);
        }

        FacebookUser u = new FacebookUser();
        u.setFacebookId("9788775675676565");
        u.setFacebookName("Le Minh Duc Vt");
        u.setFacebookProfilePic("/img/placeholder-facebook-2.jpg");
        if (!facebookUserRepository.existsById("9788775675676565")) {
            facebookUserRepository.save(u);
        }

        FacebookUser u3 = new FacebookUser();
        u3.setFacebookId("9788775642676567");
        u3.setFacebookName("Anh Hoa Dong");
        u3.setFacebookProfilePic("/img/placeholder-facebook-3.jpg");
        if (!facebookUserRepository.existsById("9788775642676567")) {
            facebookUserRepository.save(u3);
        }

        FacebookUser u4 = new FacebookUser();
        u4.setFacebookId("9788775615676567");
        u4.setFacebookName("Elise Nguyen");
        u4.setFacebookProfilePic("/img/placeholder-facebook-4.jpg");
        if (!facebookUserRepository.existsById("9788775615676567")) {
            facebookUserRepository.save(u4);
        }

        FacebookUser u5 = new FacebookUser();
        u5.setFacebookId("9788555675676567");
        u5.setFacebookName("Tran Nam Huy");
        u5.setFacebookProfilePic("/img/placeholder-facebook-5.jpg");
        if (!facebookUserRepository.existsById("9788555675676567")) {
            facebookUserRepository.save(u5);
        }
    }

    private void seedCategory() {
        Category category1 = new Category();
        category1.setCode("purchase");
        category1.setName("Mua Hàng");
        addCategoryIfMissing(category1);

        Category category2 = new Category();
        category2.setCode("complaint");
        category2.setName("Khiếu Nại");
        addCategoryIfMissing(category2);

        Category category3 = new Category();
        category3.setCode("payment");
        category3.setName("Thanh Toán");
        addCategoryIfMissing(category3);

        Category category4 = new Category();
        category4.setCode("promotion");
        category4.setName("Khuyến Mãi");
        addCategoryIfMissing(category4);

        Category category5 = new Category();
        category5.setCode("refund");
        category5.setName("Hoàn Tiền");
        addCategoryIfMissing(category5);
    }

    private void addCategoryIfMissing(Category category) {
        if (!categoryRepository.existsByCode(category.getCode())) {
            categoryRepository.save(category);
        }
    }

    private void seedPermission() {
        addPermissionIfMissing("VIEW_DASHBOARD");
        addPermissionIfMissing("VIEW_TICKET_ALL" );
        addPermissionIfMissing("VIEW_TICKET_DETAIL");
        addPermissionIfMissing("VIEW_EMPLOYEE_ALL" );
        addPermissionIfMissing("VIEW_EMPLOYEE_DASHBOARD");
    }


    private void seedUserGroup() {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(new Permission(1, "", ""));
        permissions.add(new Permission(2, "", ""));
        permissions.add(new Permission(3, "", ""));
        permissions.add(new Permission(4, "", ""));
        permissions.add(new Permission(5, "", ""));
        addGroupIfMissing("staff", permissions);
        addGroupIfMissing("supervisor", permissions);
    }


    private void seedStatus() {
        addStatusIfMissing("online");
        addStatusIfMissing("away" );
        addStatusIfMissing("offline");
    }

    private void seedEmployee() {
        Employee emp = new Employee();
        UserGroup group1 = new UserGroup();
        UserGroup group2 = new UserGroup();
        group1.setGroupId(1);
        group2.setGroupId(2);
        Status s1 = new Status();
        Status s2 = new Status();
        s1.setId(3);
        s2.setId(3);
        StatusLog log1 = new StatusLog();
        StatusLog log2 = new StatusLog();
        log1.setEmployee(emp);
        log1.setStatus(s1);
        emp.getStatusLogs().add(log1);

        emp.setUsername("binhbk");
        emp.setUserGroup(group1);
        emp.setPassword(passwordEncoder.encode("Abcd@1234"));
        emp.setName("Bui Khac Binh");
        emp.setActive(true);

        Employee emp2 = new Employee();
        emp2.setUsername("admin");
        emp2.setUserGroup(group2);
        emp2.setPassword(passwordEncoder.encode("Abcd@1234"));
        emp2.setName("Admin");
        emp2.setActive(true);
        log2.setEmployee(emp2);
        log2.setStatus(s2);
        emp2.getStatusLogs().add(log2);

        addEmployeeIfMissing(emp);
        addEmployeeIfMissing(emp2);
    }


    private void seedProgressStatuses() {
        addProgressIfMissing("pending", "Đang xử lý");
        addProgressIfMissing("on-hold", "Đang chờ");
        addProgressIfMissing("resolved", "Đã xử lý");
    }

    private void seedCustomerEmotions() {
        addEmotionIfMissing("angry", "Tức giận");
        addEmotionIfMissing("negative", "Tiêu cực");
        addEmotionIfMissing("neutral", "Trung lập");
        addEmotionIfMissing("positive", "Tích cực");
        addEmotionIfMissing("happy", "Hài lòng");
    }

    private void seedCustomerSatisfactions() {
        addSatisfactionIfMissing("verybad", "Rất Tệ");
        addSatisfactionIfMissing("unhappy", "Không Vui");
        addSatisfactionIfMissing("neutral", "Trung Lập");
        addSatisfactionIfMissing("pleased", "Tạm Được");
        addSatisfactionIfMissing("happy", "Rất Vui");
    }

    private void addProgressIfMissing(String code, String name) {
        if (!progressStatusRepository.existsByCode(code)) {
            progressStatusRepository.save(new ProgressStatus(0, code, name));
        }
    }

    private void addEmployeeIfMissing(Employee employee) {
        if (!employeeRepository.existsByUsername(employee.getUsername())) {
            employeeRepository.save(employee);
        }
    }

    private void addStatusIfMissing(String name) {
        if (!statusRepository.existsByName(name)) {
            statusRepository.save(new Status(0, name, null));
        }
    }


    private void addEmotionIfMissing(String code, String name) {
        if (!emotionRepository.existsByCode(code)) {
            emotionRepository.save(new Emotion(0, code, name));
        }
    }

    private void addSatisfactionIfMissing(String code, String name) {
        if (!satisfactionRepository.existsByCode(code)) {
            satisfactionRepository.save(new Satisfaction(0, code, name));
        }
    }

    private void addPermissionIfMissing(String permissiom) {
        if (!permissionRepository.existsByName(permissiom)) {
            permissionRepository.saveAndFlush(new Permission(0, permissiom, "test"));
        }
    }

    private void addGroupIfMissing(String name, Set<Permission> permissions) {
        if (!userGroupRepository.existsByName(name)) {
            userGroupRepository.saveAndFlush(new UserGroup(0, name, "test", null, permissions));
        }
    }

    public void seedFacebookUsers(int num) {
        Faker faker = new Faker(new Locale("vi")); // Vietnamese locale
        Random random = new Random();

        for (int i = 0; i < num; i++) {
            FacebookUser user = new FacebookUser();

            String facebookId = String.format(String.format("1234567890123%d", i));
            String realName = faker.name().fullName(); // e.g. "Khắc Bình"
            String facebookName = "FbUser " + faker.name().lastName(); // e.g. "FbUser Bùi"
            String email = faker.internet().emailAddress();
            String phone = String.format("0%09d", random.nextInt(1_0000_0000)); // e.g. 0987654321
            String zalo = String.format("0%09d", random.nextInt(1_0000_0000));
            Instant createdAt = faker.date()
                    .between(Date.from(Instant.now().minusSeconds(3600 * 24 * 365)), Date.from(Instant.now()))
                    .toInstant();

            user.setFacebookId(facebookId);
            user.setFacebookName(facebookName);
            user.setRealName(realName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setZalo(zalo);
            user.setCreatedAt(createdAt);
            user.setFacebookProfilePic(String.format("img/placeholder-facebook-%d.jpg",  (i % 5)+1 ));


            boolean result = facebookUserRepository.existsById(user.getFacebookId());
            log.info("seeding user {}", result);
            if (!facebookUserRepository.existsById(user.getFacebookId())) {
                facebookUserRepository.save(user);
            }
        }
    }

    public void seedTickets(int num) {
        List<Ticket> ticketList = ticketRepository.findAll();
        if (!ticketList.isEmpty()) {
            return;
        }
        List<FacebookUser> facebookUsers = facebookUserRepository.getAll();
        List<Employee> employeeList = cache.getAllEmployees() == null ? employeeRepository.findAll() :
                cache.getAllEmployees().values().stream().toList();
        List<Category> categoryList = cache.getAllCategories() == null ? categoryRepository.findAll() :
                cache.getAllCategories().values().stream().toList();
        List<ProgressStatus> progressStatusList = cache.getAllProgress() == null ? progressStatusRepository.findAll() :
                cache.getAllProgress().values().stream().toList();

        for (int i = 0; i < num; i++) {
            Faker faker = new Faker(new Locale("vi")); // Vietnamese locale
            Ticket ticket = new Ticket();
            ticket.setTitle(null); // random string
            ticket.setAssignee(employeeList.get(i % 2)); //get random from employeeList
            ticket.setFacebookUser(facebookUsers.get( i % facebookUsers.size()));
            ticket.setCategory(categoryList.get(i % 5));
            ticket.setProgressStatus(progressStatusList.get(i % 3));

            Instant createdAt = faker.date()
                    .between(Date.from(Instant.now().minusSeconds(3600 * 24 * 365)), Date.from(Instant.now()))
                    .toInstant();

            ticket.setCreatedAt(Timestamp.from(createdAt));

            if (!ticketRepository.existsById(ticket.getId())) {
                ticketRepository.save(ticket);
            }
        }
    }


}

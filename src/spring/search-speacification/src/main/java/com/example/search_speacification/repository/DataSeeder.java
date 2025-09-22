package com.example.search_speacification.repository;

import com.example.search_speacification.entity.Address;
import com.example.search_speacification.entity.Student;
import com.example.search_speacification.entity.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final AddressRepository addressRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    private final List<String> cities = List.of(
            "Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Huế", "Cần Thơ",
            "Hải Phòng", "Nha Trang", "Quảng Ninh", "Vinh", "Đà Lạt"
    );

    private final List<String> firstNames = List.of(
            "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Phan", "Vũ", "Đặng", "Bùi", "Đỗ"
    );

    private final List<String> middleNames = List.of(
            "Văn", "Thị", "Hữu", "Quang", "Thanh", "Ngọc", "Minh", "Tuấn", "Thành", "Đức"
    );

    private final List<String> lastNames = List.of(
            "Nam", "Lan", "Hoa", "Hùng", "Phương", "Mai", "Khánh", "Tuấn", "Dũng", "Thảo",
            "Hà", "Linh", "Trang", "Huyền", "Phong", "Tâm", "Nhung", "Thắng", "Bình", "Long"
    );

    private final List<String> csSubjects = List.of(
            "Lập trình C", "Java cơ bản", "Cấu trúc dữ liệu", "Giải thuật",
            "Hệ quản trị CSDL", "Hệ điều hành", "Mạng máy tính", "Trí tuệ nhân tạo",
            "Học máy", "Học sâu", "Xử lý ngôn ngữ tự nhiên", "Thị giác máy tính",
            "Kỹ nghệ phần mềm", "Phát triển Web", "Phát triển Mobile", "An toàn thông tin",
            "Điện toán đám mây", "Hệ phân tán", "Trình biên dịch", "Lý thuyết tính toán",
            "Tương tác người - máy", "Phân tích dữ liệu lớn", "Khai phá dữ liệu",
            "Tìm kiếm thông tin", "Máy tính lượng tử", "Blockchain", "Internet of Things",
            "Điện toán song song", "Tính toán hiệu năng cao", "Hệ nhúng", "Xử lý ảnh số"
    );

    @Override
    public void run(String... args) {
        if (studentRepository.count() > 0) {
            System.out.println("✅ Data already exists, skipping seeding...");
            return;
        }

        Random random = new Random();

        // 1. Tạo 50 địa chỉ
        List<Address> addresses = new ArrayList<>();
        for (String city : cities) {  // cities có đúng 10 phần tử
            Address address = new Address();
            address.setCity(city);
            addresses.add(address);
        }
        addressRepository.saveAll(addresses);

        // 2. Tạo 200 sinh viên
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            String fullName = firstNames.get(random.nextInt(firstNames.size())) + " " +
                    middleNames.get(random.nextInt(middleNames.size())) + " " +
                    lastNames.get(random.nextInt(lastNames.size()));

            Student student = new Student();
            student.setName(fullName);

            // random 1 address
            Address address = addresses.get(random.nextInt(addresses.size()));
            student.setAddress(address);

            students.add(student);
        }
        studentRepository.saveAll(students);

        // 3. Tạo môn học cho mỗi sinh viên
        List<Subject> subjects = new ArrayList<>();
        for (Student student : students) {
            int subjectCount = 5 + random.nextInt(6); // 5–10 môn
            for (int i = 0; i < subjectCount; i++) {
                String subjectName = csSubjects.get(random.nextInt(csSubjects.size()));

                Subject subject = new Subject();
                subject.setName(subjectName);
                subject.setStudent(student); // ✅ sửa lại, không dùng studentId nữa

                subjects.add(subject);
            }
        }
        subjectRepository.saveAll(subjects);

        System.out.println("✅ Seeded " + addresses.size() + " addresses, " +
                students.size() + " students, " +
                subjects.size() + " subjects");
    }
}


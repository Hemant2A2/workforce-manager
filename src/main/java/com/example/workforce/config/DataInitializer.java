package com.example.workforce.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;

import com.example.workforce.models.*;
import com.example.workforce.enums.Title;
import com.example.workforce.enums.Gender;
import com.example.workforce.repositories.*;


@Component
public class DataInitializer implements ApplicationRunner {

  private final MemberRepository memberRepository;
  private final MemberTypeRepository memberTypeRepository;
  private final LocationRepository locationRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  @Value("${app.seed.enabled:true}")
  private boolean seedEnabled;

  @Value("${app.seed.output-file:./dummy-credentials.csv}")
  private String credentialsPath;

  public DataInitializer(MemberRepository memberRepository,
                         MemberTypeRepository memberTypeRepository,
                         LocationRepository locationRepository,
                         PasswordEncoder passwordEncoder,
                         RoleRepository roleRepository) {
    this.memberRepository = memberRepository;
    this.memberTypeRepository = memberTypeRepository;
    this.locationRepository = locationRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {
    if (!seedEnabled) {
      System.out.println("[DataInitializer] seed disabled (app.seed.enabled=false).");
      return;
    }

    long existing = memberRepository.count();
    if (existing > 0) {
      System.out.println("[DataInitializer] members already exist (" + existing + "). skipping seeding.");
      return;
    }

    System.out.println("[DataInitializer] seeding initial member types, members and locations...");

    seedDatabase();
  }

  @Transactional
  protected void seedDatabase() {
    try {
      Map<String, String> createdCredentials = new LinkedHashMap<>();

      MemberType adminType = memberTypeRepository.findByTitle(Title.ADMIN)
          .orElseGet(() -> memberTypeRepository.save(buildMemberType(Title.ADMIN, 560, 30)));

      MemberType managerType = memberTypeRepository.findByTitle(Title.MANAGER)
          .orElseGet(() -> memberTypeRepository.save(buildMemberType(Title.MANAGER, 160, 30)));

      MemberType memberType = memberTypeRepository.findByTitle(Title.MEMBER)
          .orElseGet(() -> memberTypeRepository.save(buildMemberType(Title.MEMBER, 160, 24)));


      String adminPassword = randomPassword(12);
      Member admin = new Member();
      admin.setFName("Alice");
      admin.setMName("A");
      admin.setLName("Admin");
      admin.setGender(Gender.FEMALE);
      admin.setApartment("HQ");
      admin.setCity("Mumbai");
      admin.setDob(LocalDate.of(1985, 1, 1));
      admin.setPassword(passwordEncoder.encode(adminPassword));
      admin.setOvertimeRequired(0.0);
      admin.setPhone("9000000001");
      admin.setAvailedLeaves(0);
      admin.setMemberType(adminType);
      admin = memberRepository.save(admin);

      createdCredentials.put("admin:" + admin.getId(), adminPassword);


      List<String[]> managers = Arrays.asList(
          new String[]{"Bob", "B", "Manager", "Mumbai", "9000000011"},
          new String[]{"Charlie", "C", "Manager", "Pune", "9000000012"},
          new String[]{"David", "D", "Manager", "Bengaluru", "9000000013"},
          new String[]{"Eve", "E", "Manager", "Chennai", "9000000014"},
          new String[]{"Frank", "F", "Manager", "Kolkata", "9000000015"}
      );

      List<Member> savedManagers = new ArrayList<>();
      Role managerRole = roleRepository.findById(1)
          .orElseThrow(() -> new IllegalStateException("MANAGER role not found in DB"));
      for (int i = 0; i < managers.size(); i++) {
        String[] m = managers.get(i);
        String pwd = randomPassword(12);
        Member mm = new Member();
        mm.setFName(m[0]);
        mm.setMName(m[1]);
        mm.setLName(m[2]);
        mm.setGender(Gender.MALE);
        mm.setApartment("Apt " + (i + 2));
        mm.setCity(m[3]);
        mm.setDob(LocalDate.of(1986 + i, 2 + i, 10 + i));
        mm.setPassword(passwordEncoder.encode(pwd));
        mm.setOvertimeRequired(0.0);
        mm.setPhone(m[4]);
        mm.setAvailedLeaves(0);
        mm.setMemberType(managerType);
        mm.getFeasibleRoles().add(managerRole);
        mm = memberRepository.save(mm);
        createdCredentials.put("manager:" + mm.getId(), pwd);
        savedManagers.add(mm);
      }

      List<Location> locations = new ArrayList<>();
      Location l1 = new Location();
      l1.setPlotNo("12A");
      l1.setStreet("Maple Street");
      l1.setCity("Mumbai");
      l1.setPincode("400001");
      l1.setManager(savedManagers.get(0));
      locations.add(l1);

      Location l2 = new Location();
      l2.setPlotNo("7B");
      l2.setStreet("Oak Avenue");
      l2.setCity("Pune");
      l2.setPincode("411001");
      l2.setManager(savedManagers.get(4));
      locations.add(l2);

      Location l3 = new Location();
      l3.setPlotNo("101");
      l3.setStreet("Cedar Lane");
      l3.setCity("Bengaluru");
      l3.setPincode("560001");
      l3.setManager(savedManagers.get(2));
      locations.add(l3);

      Location l4 = new Location();
      l4.setPlotNo("45");
      l4.setStreet("Pine Road");
      l4.setCity("Chennai");
      l4.setPincode("600001");
      l4.setManager(savedManagers.get(1));
      locations.add(l4);

      Location l5 = new Location();
      l5.setPlotNo("9C");
      l5.setStreet("Birch Boulevard");
      l5.setCity("Kolkata");
      l5.setPincode("700001");
      l5.setManager(savedManagers.get(3));
      locations.add(l5);

      for (Location loc : locations) {
        Location saved = locationRepository.save(loc);
        Member mgr = saved.getManager();
        if (mgr != null) {
          mgr.setWorksAt(saved);
          memberRepository.save(mgr);
        }
      }

      if (!locations.isEmpty()) {
        // find first saved location from DB to be safe
        Location firstLoc = locationRepository.findById(locations.get(0).getId())
            .orElse(locationRepository.findAll().stream().findFirst().orElse(null));
        if (firstLoc != null) {
          admin.setWorksAt(firstLoc);
          memberRepository.save(admin); // persist the admin's worksAt FK
          System.out.println("[DataInitializer] assigned admin (id=" + admin.getId() + ") to location id=" + firstLoc.getId());
        }
      }


      writeCredentialsFile(createdCredentials, credentialsPath);

      System.out.println("[DataInitializer] seeding complete. Credentials written to: " + credentialsPath);

    } catch (Exception ex) {
      System.err.println("[DataInitializer] seeding failed: " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  private MemberType buildMemberType(Title t, Integer allowedHours, Integer allowedPaid) {
    MemberType mt = new MemberType();
    mt.setTitle(t);
    mt.setAllowedHours(allowedHours);
    mt.setAllowedPaidLeaves(allowedPaid);
    return mt;
  }

  private void writeCredentialsFile(Map<String, String> creds, String pathStr) throws IOException {
    Path path = Path.of(pathStr).toAbsolutePath();
    List<String> lines = new ArrayList<>();
    lines.add("principal,id,password");
    for (Map.Entry<String,String> e : creds.entrySet()) {
      String principalAndId = e.getKey();
      String[] parts = principalAndId.split(":");
      String principal = parts[0];
      String id = parts.length > 1 ? parts[1] : "";
      lines.add(principal + "," + id + "," + e.getValue());
    }
    Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final SecureRandom random = new SecureRandom();

  private String randomPassword(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
    }
    return sb.toString();
  }
}

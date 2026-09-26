
# Room-wise Timetable Generator

I made this program to make a university timetable easier to view by organizing it room-wise instead of section-wise.

The university website provides timetable information for different sections. This program scrapes that information, reorganizes the timetable entries by room, and generates a PDF containing the resulting room-wise timetables.

## What It Does

The program:

- Gets the available departments and sections from the university timetable website.
- Scrapes the timetable for each section.
- Processes the timetable information and groups classes by room.
- Generates a room-wise timetable as a PDF.
- Also saves the processed section-wise and room-wise data as JSON files.

## How It Works

The basic process is:

1. Get the section-wise timetable information from the university website.
2. Read the classes, rooms, teachers, courses, days, and periods.
3. Reorganize the entries so that classes are grouped by room.
4. Generate a PDF with a separate timetable for each room.

The generated PDF is designed to make it easier to see which classes are scheduled in each room throughout the week.

## Usage / Basic Setup

This project is written in Kotlin and uses Gradle.

To use it, you will need a suitable Kotlin/JVM development environment and the project's dependencies. The program can then be run from the project environment.

No additional configuration is documented here because the exact setup may depend on your development environment.

## Output

The program generates:

- `room_timetable.pdf` — the room-wise timetable.
- `section_timetable.json` — the timetable data collected for each section.
- `room_timetable.json` — the processed room-wise timetable data.

The PDF contains each room's timetable, including days, periods, course information, sections, and teachers.

## Notes / Limitations

This program depends on the structure and availability of the university's timetable website.

If the website changes its HTML structure, timetable format, URLs, or the way it provides department and section information, the program may need to be changed to work with the new format.

There are also some specific timetable cases handled in the code for correcting data that does not fit the normal timetable format.

## Educational Purpose

I made this project mainly for learning and educational purposes. It gave me practice with web scraping, processing structured data, reorganizing timetable information, and generating PDFs.

## Disclaimer

This project is not affiliated with, sponsored by, or endorsed by the university.

Please use it responsibly and respect the university website's terms, policies, and reasonable request limits when using the program.

## License

This project is licensed under the MIT License.

### MIT License

Copyright (c) 2026

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

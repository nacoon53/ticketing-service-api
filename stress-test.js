import http from 'k6/http';
import { check, sleep } from 'k6';

const virtual_users = 5000;

export const options = {
    vus: virtual_users,    // 가상의 유저 수
    duration: '30s'       // 테스트 시간
};

const users = [];
for (let i = 1; i <= virtual_users; i++) {
    users.push({
        USER_ID: `btesDtUser${i}`,
        WAITLIST_TOKEN: ''
    });
}

const baseHeaders = {
    'Content-Type': 'application/json'
};

export default function () {
    const user = users[__VU - 1]; // VU 번호에 맞는 유저 선택
    const headers = {
        ...baseHeaders,
        USER_ID: user.USER_ID
    };

    // 1. 콘서트 목록 조회 (최초 1회만 실행)
    if (!user.WAITLIST_TOKEN) {
        const response = http.get('http://localhost:8080/api/v1/concerts', { headers: headers });

        const tokenExtracted = check(response, {
            'Concert List Status is 403': (r) => r.status === 403,
            'Waitlist_token exists': (r) => r.headers['Waitlist_token'] !== undefined
        });

        if (tokenExtracted) {
            user.WAITLIST_TOKEN = response.headers['Waitlist_token'];
            headers['WAITLIST_TOKEN'] = user.WAITLIST_TOKEN;
            console.log(`User: ${user.USER_ID}, Token: ${user.WAITLIST_TOKEN}`);
        } else {
            console.error(`User: ${user.USER_ID} - Token not found`);
        }
    }

    // 2. 좌석 조회 요청 (반복 실행)
    headers['WAITLIST_TOKEN'] = user.WAITLIST_TOKEN; // 저장된 토큰 사용

    const seatResponse = http.get('http://localhost:8080/api/v1/concerts/1/seats?status=available', { headers: headers });

    check(seatResponse, {
        'Seat List Status is 200': (r) => r.status === 200
    });

    console.log(`User: ${user.USER_ID}, Seats Response Time: ${seatResponse.timings.duration} ms`);

    sleep(1); // 1초 대기 후 반복 요청
}

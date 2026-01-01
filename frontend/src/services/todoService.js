import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api/todos';

export const todoService = {
    // 전체 조회
    getAllTodos() {
        return axios.get(API_BASE_URL);
    },

    // 완료 상태별 조회
    getTodosByCompleted(completed) {
        return axios.get(API_BASE_URL, {
            params: { completed }
        });
    },

    // 단건 조회
    getTodoById(id) {
        return axios.get(`${API_BASE_URL}/${id}`);
    },

    // 생성
    createTodo(todoData) {
        return axios.post(API_BASE_URL, todoData);
    },

    // 수정
    updateTodo(id, todoData) {
        return axios.put(`${API_BASE_URL}/${id}`, todoData);
    },

    // 완료 상태 토글
    toggleTodo(id) {
        return axios.patch(`${API_BASE_URL}/${id}/toggle`);
    },

    // 삭제
    deleteTodo(id) {
        return axios.delete(`${API_BASE_URL}/${id}`);
    },

    // 일괄 삭제
    deleteMultipleTodos(ids) {
        return axios({
            method: 'delete',
            url: `${API_BASE_URL}/batch`,
            data: ids,
            headers: {
                'Content-Type': 'application/json'
            }
        });
    }
};

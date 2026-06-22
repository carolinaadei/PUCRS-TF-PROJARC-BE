export interface AuthRequest {
    headers: {
        authorization?: string;
    };
    body: any;
}

export interface AuthResponse {
    status: number;
    message: string;
    data?: any;
}